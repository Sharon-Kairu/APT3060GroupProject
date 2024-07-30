package com.example.apt3060groupproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserItemsAdapter userItemsAdapter;
    private FirebaseFirestore db;
    private List<CartItem> cartItems = new ArrayList<>();
    private String userId;
    private String branchId;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_items);

        recyclerView = findViewById(R.id.recyclerView_user_items);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get user ID and branch ID from intent
        userId = getIntent().getStringExtra("USER_ID");
        branchId = getIntent().getStringExtra("BRANCH_ID");

        userItemsAdapter = new UserItemsAdapter(cartItems);
        recyclerView.setAdapter(userItemsAdapter);

        db = FirebaseFirestore.getInstance();

        if (userId != null && branchId != null) {
            fetchUserCartItems();
        } else {
            Log.d("UserItemsActivity", "User ID or Branch ID is missing.");
        }
    }

    private void fetchUserCartItems() {
        CollectionReference cartItemsRef = db.collection("branches").document(branchId).collection("cart_items");

        cartItemsRef.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    cartItems.clear();
                    double totalAmount = 0.0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CartItem cartItem = document.toObject(CartItem.class);
                        cartItems.add(cartItem);
                        totalAmount += cartItem.getPrice() * cartItem.getQuantity();
                    }
                    userItemsAdapter.notifyDataSetChanged();
                    totalAmountTextView.setText(String.format("Total Amount: ", totalAmount));
                } else {
                    Log.d("Error", "Error getting cart items: ", task.getException());
                }
            }
        });
    }
}
