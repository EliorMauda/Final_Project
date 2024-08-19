package com.example.finalproject.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.Adapter.CartAdapter;
import com.example.finalproject.Adapter.PopularAdapter;
import com.example.finalproject.Adapter.WishListAdapter;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.databinding.ActivityCartBinding;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCartAndWish managmentCartAndWish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String discountCode = getIntent().getStringExtra("discount_code");
        if (discountCode == null)
            discountCode = "";

        managmentCartAndWish = new ManagmentCartAndWish(this, discountCode);

        calculatorCart();
        setVariable();
        initCartList();
    }


    private void initCartList() {
        if (managmentCartAndWish.getListCart("CartList").isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            if (!managmentCartAndWish.getDiscountCode().isEmpty()) {
                binding.editTextText2.setText(managmentCartAndWish.getDiscountCode());
                binding.editTextText2.setTextColor(Color.RED);
                binding.editTextText2.setTypeface(null, Typeface.BOLD);
            }
            binding.editTextText2.setOnClickListener(v -> {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                CartActivity.this.startActivity(intent);
            });
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCartAndWish.getListCart("CartList"), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        if (managmentCartAndWish.getListCart("CartList").isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }

        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCartAndWish.getTotalCartFee("CartList") * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCartAndWish.getTotalCartFee("CartList") + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managmentCartAndWish.getTotalCartFee("CartList") * 100.0)) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);

        int discountNumber = getIntent().getIntExtra("discount_number", 0);
        if (discountNumber != 0) {
            binding.applyBtn.setOnClickListener(v -> {
                double discountedTotal = total * (1 - (double) discountNumber / 100);
                binding.totalTxt.setText("$" + discountedTotal);
            });
        }
    }

}