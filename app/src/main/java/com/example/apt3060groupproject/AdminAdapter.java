package com.example.apt3060groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<Branch> branchList;
    private OnBranchClickListener onBranchClickListener;

    public AdminAdapter(List<Branch> branchList, OnBranchClickListener listener) {
        this.branchList = branchList;
        this.onBranchClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_branch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Branch branch = branchList.get(position);
        holder.textView.setText(branch.getName());

        holder.itemView.setOnClickListener(v -> {
            if (onBranchClickListener != null) {
                onBranchClickListener.onBranchClick(branch);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.branchName);
        }
    }

    public interface OnBranchClickListener {
        void onBranchClick(Branch branch);
    }
}
