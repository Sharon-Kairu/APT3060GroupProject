package com.example.apt3060groupproject;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    RecyclerView selections;
    LinearLayout top;
    TextView empty_cart, total;
    FirebaseFirestore db;
    private List<CartItem> cartItemList;
    CartAdapter cartAdapter;
    Button checkout;
    private String userId;
    private String branchId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);

        top = findViewById(R.id.linearLayout);
        selections = findViewById(R.id.cart_recycler_view);
        selections.setLayoutManager(new LinearLayoutManager(this));

        total = findViewById(R.id.total);
        db = FirebaseFirestore.getInstance();
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList, this);
        selections.setAdapter(cartAdapter);

        userId = getIntent().getStringExtra("userId");
        branchId = getIntent().getStringExtra("selectedBranchID");

        loadCartItems();

        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Checkout.class);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        if (userId != null && branchId != null) {
            db.collection("branches")
                    .document(branchId)
                    .collection("cart_items")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cartItemList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartItem item = document.toObject(CartItem.class);
                                item.setId(document.getId());
                                cartItemList.add(item);
                            }
                            Log.d(TAG, "Cart items loaded: " + cartItemList.size());
                            updateUI();
                            updateTotalCost();
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "User ID or Branch ID is null");
        }
    }

    private void updateUI() {

            top.setVisibility(View.VISIBLE);
            selections.setVisibility(View.VISIBLE);
            cartAdapter.notifyDataSetChanged();

    }

    private void updateTotalCost() {
        double totalCost = calculateTotalCost(cartItemList);
        String totalCostString = String.valueOf(totalCost);
        total.setText(totalCostString);
    }

    private double calculateTotalCost(List<CartItem> cartItems) {
        double totalCost = 0.0;
        for (CartItem item : cartItems) {
            totalCost += item.getPrice() * item.getQuantity();
        }
        return totalCost;
    }

    public void onItemDeleted() {
        updateTotalCost();
    }
}
