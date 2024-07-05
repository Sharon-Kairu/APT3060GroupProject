package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {
    private final List<Branch> branchList;
    private final OnBranchClickListener onBranchClickListener;

    public BranchAdapter(List<Branch> branchList, OnBranchClickListener onBranchClickListener) {
        this.branchList = branchList;
        this.onBranchClickListener = onBranchClickListener;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        Branch branch = branchList.get(position);
        holder.branchNameTextView.setText(branch.getName());
        holder.locationTextView.setText(branch.getLocation());
        holder.itemView.setOnClickListener(v -> onBranchClickListener.onBranchClick(branch));
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public static class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView branchNameTextView;
        TextView locationTextView;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            branchNameTextView = itemView.findViewById(R.id.branch_name);
            locationTextView = itemView.findViewById(R.id.branch_location);
        }
    }

    public interface OnBranchClickListener {
        void onBranchClick(Branch branch);
    }
}
