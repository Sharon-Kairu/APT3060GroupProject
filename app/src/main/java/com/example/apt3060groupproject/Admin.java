package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity implements AdminAdapter.OnBranchClickListener {

    private RecyclerView recyclerView;
    private TextView view_your_orders;
    private AdminAdapter adapter;
    private FirebaseFirestore db;
    private List<Branch> branchList = new ArrayList<>();
    private List<String> branchIdsWithOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView_branches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        view_your_orders = findViewById(R.id.text);

        db = FirebaseFirestore.getInstance();

        adapter = new AdminAdapter(branchList, this); // Correctly passing 'this' as listener
        recyclerView.setAdapter(adapter);

        fetchBranchesWithOrders(); // Fetch branches with orders
    }

    private void fetchBranchesWithOrders() {
        db.collection("branches")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            branchIdsWithOrders.clear();
                            List<Task<QuerySnapshot>> cartTasks = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CollectionReference cartItemsRef = document.getReference().collection("cart_items");
                                Task<QuerySnapshot> cartTask = cartItemsRef.get();
                                cartTasks.add(cartTask);
                            }

                            Tasks.whenAllSuccess(cartTasks).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                                @Override
                                public void onComplete(@NonNull Task<List<Object>> task) {
                                    if (task.isSuccessful()) {
                                        List<Object> results = task.getResult();
                                        for (int i = 0; i < results.size(); i++) {
                                            QuerySnapshot cartSnapshot = (QuerySnapshot) results.get(i);
                                            if (!cartSnapshot.isEmpty()) {
                                                String branchId = cartSnapshot.getDocuments().get(0).getReference().getParent().getParent().getId();
                                                if (!branchIdsWithOrders.contains(branchId)) {
                                                    branchIdsWithOrders.add(branchId);
                                                }
                                            }
                                        }
                                        fetchBranches(); // Fetch branches after collecting IDs with orders
                                    } else {
                                        Log.d("Error", "Error getting cart documents: ", task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.d("Error", "Error getting branch documents: ", task.getException());
                        }
                    }
                });
    }

    private void fetchBranches() {
        db.collection("branches")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            branchList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                if (branchIdsWithOrders.contains(id)) {
                                    String name = document.getString("name");
                                    Branch branch = new Branch();
                                    branch.setId(id);
                                    branch.setName(name);
                                    branchList.add(branch);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Error", "Error getting branch documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBranchClick(Branch branch) {
        Intent intent = new Intent(Admin.this, BranchItems.class);
        intent.putExtra("BRANCH_ID", branch.getId());
        startActivity(intent);
    }
}
