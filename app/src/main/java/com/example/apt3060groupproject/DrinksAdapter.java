package com.example.apt3060groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksViewHolder> {
    private final List<Drink> drinksList;
    private final OnDrinkClickListener onDrinkClickListener;

    public DrinksAdapter(List<Drink> drinksList, OnDrinkClickListener onDrinkClickListener) {
        this.drinksList = drinksList;
        this.onDrinkClickListener = onDrinkClickListener;
    }

    @NonNull
    @Override
    public DrinksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_item, parent, false);
        return new DrinksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinksViewHolder holder, int position) {
        Drink drink = drinksList.get(position);
        holder.drinkNameTextView.setText(drink.getDrinkName());
        holder.drinkImageImageView.setImageResource(drink.getDrinkImage());
        holder.itemView.setOnClickListener(v -> onDrinkClickListener.onDrinkClick(drink));
    }

    @Override
    public int getItemCount() {
        return drinksList.size();
    }

    public static class DrinksViewHolder extends RecyclerView.ViewHolder {

        ImageView drinkImageImageView;
        TextView drinkNameTextView;
        TextView drinkBrandTextView;

        public DrinksViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name);
            drinkImageImageView=itemView.findViewById(R.id.drink_image);

        }
    }

    public interface OnDrinkClickListener {
        void onDrinkClick(Drink drink);
    }
}
