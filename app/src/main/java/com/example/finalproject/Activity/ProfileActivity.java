package com.example.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.R;
import com.example.finalproject.Utilities.SoundPlayer;
import com.example.finalproject.databinding.ActivityProfileBinding;
import com.example.finalproject.databinding.ActivitySignInBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    SoundPlayer soundPlayer;
    private ManagmentCartAndWish managmentCartAndWish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        soundPlayer = SoundPlayer.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("sound_prefs", MODE_PRIVATE);
        boolean isSoundEnabled = prefs.getBoolean("sound_enabled", false);
        binding.switch1.setChecked(isSoundEnabled);

        if (isSoundEnabled) {
            soundPlayer.playSound(R.raw.backgroundmusic);
        }

        managmentCartAndWish = new ManagmentCartAndWish(this, "");


        initViews();
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
//            DatabaseReference myRef = database.getReference("geust");
//            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    HashMap<String, Object> userData = new HashMap<>();
//                    ArrayList<ItemsDomain> cartDetails = managmentCartAndWish.getListCart("CartList");
//                    ArrayList<ItemsDomain> wishListDetails = managmentCartAndWish.getListCart("WishList");
//                    userData.put("cart", cartDetails);
//                    userData.put("wishList", wishListDetails);
//                    myRef.updateChildren(userData);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
    }

    private void initViews() {
        isPhoneNumber();

        binding.logOutBtn.setOnClickListener(v -> signOut());

        binding.phone.setOnClickListener(v -> {
            // Create an intent to dial the phone number
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + "0523456789"));
            startActivity(dialIntent);
        });

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences("sound_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("sound_enabled", isChecked);
            editor.apply();

            if (isChecked) {
                soundPlayer.playSound(R.raw.backgroundmusic);
            } else {
                soundPlayer.stopSound();
            }
        });
    }

    private void isPhoneNumber() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean isPhoneProvider = false;
        // Check if the user signed in with a phone number
        if (user != null) {
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getProviderId().equals("phone")) {
                    isPhoneProvider = true;
                    break;
                }
            }

            if (!isPhoneProvider) {
                Glide
                        .with(this)
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .into(binding.mainIMGImage);
                binding.mainLBLName.setText(user.getDisplayName());
                binding.mainLBLEmail.setText(user.getEmail());
                binding.mainLBLPhone.setText(user.getPhoneNumber());
            } else {
                binding.mainLBLName.setVisibility(View.GONE);
                binding.mainLBLEmail.setVisibility(View.GONE);
                binding.cardView1.setVisibility(View.GONE);
                binding.mainLBLPhone.setText(user.getPhoneNumber());
            }
        } else {
            binding.mainLBLName.setVisibility(View.GONE);
            binding.mainLBLEmail.setText("Hello Geust!");
            binding.mainLBLPhone.setVisibility(View.GONE);
            binding.cardView1.setVisibility(View.GONE);
            binding.cardView2.setVisibility(View.GONE);
            binding.logOutBtn.setVisibility(View.GONE);


        }
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(ProfileActivity.this, IntroActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        saveUserData();
    }
}