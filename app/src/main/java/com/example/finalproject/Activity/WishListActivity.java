package com.example.finalproject.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.Adapter.CartAdapter;
import com.example.finalproject.Adapter.WishListAdapter;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityCartBinding;
import com.example.finalproject.databinding.ActivityWishListBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WishListActivity extends AppCompatActivity {
    ActivityWishListBinding binding;
    private double tax;
    private ManagmentCartAndWish managmentCartAndWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCartAndWish = new ManagmentCartAndWish(this, "");

        calculatorCart();
        setVariable();
        initCartList();
        addToTheCart();

    }

    private void addToTheCart() {
        binding.AddToCartBtn.setOnClickListener(v -> {
            if (managmentCartAndWish.getListCart("WishList") != null && !managmentCartAndWish.getListCart("WishList").isEmpty()) {
                for (ItemsDomain item :  managmentCartAndWish.getListCart("WishList")) {
                    item.setNumberinCart(item.getNumberinWishList());
                    managmentCartAndWish.insertItem(item, "CartList");
                }
                managmentCartAndWish.deleteAll(managmentCartAndWish.getListCart("WishList"), "WishList",() -> calculatorCart());
                binding.emptyTxt.setVisibility(View.VISIBLE);
                binding.scrollViewCart.setVisibility(View.GONE);
            }
        });
    }


    private void initCartList() {
        if (managmentCartAndWish.getListCart("WishList").isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new WishListAdapter(managmentCartAndWish.getListCart("WishList"), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        if (managmentCartAndWish.getListCart("WishList").isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }

        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCartAndWish.getTotalWishListFee("WishList") * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCartAndWish.getTotalWishListFee("WishList") + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managmentCartAndWish.getTotalWishListFee("WishList") * 100.0)) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}