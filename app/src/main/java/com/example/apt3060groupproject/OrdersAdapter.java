package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<String> userNames;
    private OnItemClickListener onItemClickListener;

    public OrdersAdapter(List<String> userNames, OnItemClickListener onItemClickListener) {
        this.userNames = userNames;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userName = userNames.get(position);
        holder.userNameTextView.setText(userName);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(userName));
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String userName);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}
