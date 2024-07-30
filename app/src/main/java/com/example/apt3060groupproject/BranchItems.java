package com.example.apt3060groupproject;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private FirebaseFirestore db;
    private List<String> userNames = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();
    private String branchId;
    private TextView branchIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_items);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView_orders);
        branchIdTextView = findViewById(R.id.branchIdTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get branch ID from intent
        branchId = getIntent().getStringExtra("BRANCH_ID");

        // Set branch ID in TextView
        if (branchId != null) {
            branchIdTextView.setText("Branch: " + branchId);
        } else {
            branchIdTextView.setText("Branch ID: Not found");
        }

        // Initialize the adapter with an item click listener
        ordersAdapter = new OrdersAdapter(userNames, userName -> {
            int position = userNames.indexOf(userName);
            if (position != -1) {
                String userId = userIds.get(position);
                Intent intent = new Intent(BranchItems.this, UserItemsActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("BRANCH_ID", branchId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(ordersAdapter);

        db = FirebaseFirestore.getInstance();

        fetchOrders();
    }

    private void fetchOrders() {
        CollectionReference ordersRef = db.collection("branches").document(branchId).collection("cart_items");

        ordersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userIds.clear();
                    List<String> tempUserNames = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("userId");
                        if (userId != null && !userIds.contains(userId)) {
                            userIds.add(userId);
                            tempUserNames.add(userId); // Temporarily use userId to fetch names
                        }
                    }
                    fetchUserNames(tempUserNames);
                } else {
                    Log.d("Error", "Error getting orders: ", task.getException());
                }
            }
        });
    }

    private void fetchUserNames(List<String> userIds) {
        Map<String, String> userNameMap = new HashMap<>();
        for (String userId : userIds) {
            db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.getString("name");
                            if (userName != null) {
                                userNameMap.put(userId, userName);
                                updateRecyclerView(userNameMap);
                            }
                        }
                    } else {
                        Log.d("Error", "Error getting user document: ", task.getException());
                    }
                }
            });
        }
    }

    private void updateRecyclerView(Map<String, String> userNameMap) {
        userNames.clear();
        userIds.clear();
        for (Map.Entry<String, String> entry : userNameMap.entrySet()) {
            userIds.add(entry.getKey());
            userNames.add(entry.getValue());
        }
        ordersAdapter.notifyDataSetChanged();
    }
}
