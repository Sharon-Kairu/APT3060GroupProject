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

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    RecyclerView branches;
    List<Branch> branchlist;
    BranchAdapter branchAdapter;
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

        branches = findViewById(R.id.branches);
        branches.setLayoutManager(new LinearLayoutManager(this));

        branchlist = getbranchlist();
        branchAdapter=new BranchAdapter(branchlist, this::onBranchClick);
        branches.setAdapter(branchAdapter);

    }

        List<Branch> getbranchlist(){
            List<Branch> branches=new ArrayList<>();
            branches.add(new Branch("Headquarter" ,"Nairobi"));
            branches.add(new Branch("Branch One","Kisumu"));
            branches.add(new Branch("Branch two","Nakuru"));
            branches.add(new Branch("Branch Three", "Naivasha"));
            return branches;


    }

    void onBranchClick(Branch branch){
        Intent intent=new Intent(Home.this,Drinks.class);
        startActivity(intent);
    }
}