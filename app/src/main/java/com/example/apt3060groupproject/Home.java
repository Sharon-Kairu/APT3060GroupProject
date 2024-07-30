package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    RecyclerView branches;
    List<Branch> branchList;
    BranchAdapter branchAdapter;
    FirebaseFirestore db;

    private String userId; // Add a field to store the user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Get the user ID from the intent extras
        userId = getIntent().getStringExtra("USER_ID");

        branches = findViewById(R.id.branches);
        branches.setLayoutManager(new LinearLayoutManager(this));

        branchList = getBranchList();
        branchAdapter = new BranchAdapter(branchList, this::onBranchClick);
        branches.setAdapter(branchAdapter);

        // Add branch data to Firestore
        addBranchesToFirestore();
    }

    List<Branch> getBranchList() {
        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("headquarter", "Headquarter", "Nairobi"));
        branches.add(new Branch("branch_1", "Branch One", "Kisumu"));
        branches.add(new Branch("branch_2", "Branch Two", "Nakuru"));
        branches.add(new Branch("branch_3", "Branch Three", "Naivasha"));
        return branches;
    }

    private void addBranchesToFirestore() {
        for (Branch branch : branchList) {
            Map<String, String> branchData = new HashMap<>();
            branchData.put("name", branch.getName());
            branchData.put("location", branch.getLocation()); // Assuming Branch class has a location field

            db.collection("branches")
                    .document(branch.getId())
                    .set(branchData)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success if needed
                        System.out.println("Branch added/updated successfully: " + branch.getId());
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        System.err.println("Error adding/updating branch: " + e.getMessage());
                    });
        }
    }

    void onBranchClick(Branch branch) {
        // Navigate to Drinks activity
        Intent intent = new Intent(Home.this, Drinks.class);
        intent.putExtra("selectedBranchID", branch.getId());
        intent.putExtra("selectedBranchName", branch.getName());
        intent.putExtra("USER_ID", userId); // Pass the user ID to the Drinks activity
        startActivity(intent);
    }
}
