package com.example.finalproject.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityIntroBinding;
import com.example.finalproject.databinding.ActivitySignInBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    private ManagmentCartAndWish managmentCartAndWish;
    boolean isFirstTime = true;



    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCartAndWish = new ManagmentCartAndWish(this, "");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            signIn();
        } else {
            transactToMainActivity();
        }
    }

    private void transactToMainActivity() {
        managmentCartAndWish.deleteArray(managmentCartAndWish.getListCart("CartList"), "CartList");
        managmentCartAndWish.deleteArray(managmentCartAndWish.getListCart("WishList"), "WishList");
        loadUserData();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (user != null) {
            DatabaseReference myRef = database.getReference("users");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userKey = null;

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String existingUid = childSnapshot.child("Uid").getValue(String.class);
                        if (existingUid != null && existingUid.equals(user.getUid())) {
                            userKey = childSnapshot.getKey();
                            break;
                        }
                    }

                    if (userKey != null) {
                        DataSnapshot cartSnapshot = snapshot.child(userKey).child("cart");
                        DataSnapshot wishListSnapshot = snapshot.child(userKey).child("wishList");

                        for (DataSnapshot dataSnapshot : cartSnapshot.getChildren()) {
                            ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                managmentCartAndWish.insertItem(item, "CartList");
                            }
                        }

                        for (DataSnapshot dataSnapshot : wishListSnapshot.getChildren()) {
                            ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                managmentCartAndWish.insertItem(item, "WishList");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {

        }
    }

    private void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            transactToMainActivity();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}