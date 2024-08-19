package com.example.finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.databinding.ActivityIntroBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;
    ManagmentCartAndWish managmentCartAndWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCartAndWish = new ManagmentCartAndWish(this, "");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        binding.startBtn.setOnClickListener(v -> {
            if (user != null) {
                signOut();
            }
            startActivity(new Intent(this, MainActivity.class));
        });

        binding.singInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
            }
        });
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

        saveUserData();
    }


    private void saveUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (user != null) {
            DatabaseReference myRef = database.getReference("users");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean userExists = false;
                    String userKey = null;

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String existingUid = childSnapshot.child("Uid").getValue(String.class);
                        if (existingUid != null && existingUid.equals(user.getUid())) {
                            userExists = true;
                            userKey = childSnapshot.getKey();
                            break;
                        }
                    }

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Uid", user.getUid());
                    ArrayList<ItemsDomain> cartDetails = managmentCartAndWish.getListCart("CartList");
                    ArrayList<ItemsDomain> wishListDetails = managmentCartAndWish.getListCart("WishList");
                    userData.put("cart", cartDetails);
                    userData.put("wishList", wishListDetails);


                    if (!userExists) {
                        myRef.push().setValue(userData);
                    } else {
                        myRef.child(userKey).updateChildren(userData);
                    }

                    managmentCartAndWish.deleteArray(managmentCartAndWish.getListCart("CartList"), "CartList");
                    managmentCartAndWish.deleteArray(managmentCartAndWish.getListCart("WishList"), "WishList");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            DatabaseReference myRef = database.getReference("geust");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> userData = new HashMap<>();
                    ArrayList<ItemsDomain> cartDetails = managmentCartAndWish.getListCart("CartList");
                    ArrayList<ItemsDomain> wishListDetails = managmentCartAndWish.getListCart("WishList");
                    userData.put("cart", cartDetails);
                    userData.put("wishList", wishListDetails);
                    myRef.updateChildren(userData);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}