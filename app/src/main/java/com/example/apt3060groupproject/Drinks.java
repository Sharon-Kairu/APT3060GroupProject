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

public class Drinks extends AppCompatActivity {
    RecyclerView drinks;

    List<Drink>drinkslist;
    DrinksAdapter drinksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drinks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drinks=findViewById(R.id.drinks);
        drinks.setLayoutManager(new LinearLayoutManager(this));

        drinkslist=getDrinksList();
        drinksAdapter=new DrinksAdapter(drinkslist,this::onDrinkClick);
        drinks.setAdapter(drinksAdapter);
    }



    List<Drink> getDrinksList(){
        List<Drink> drinks=new ArrayList<>();
        drinks.add(new Drink("Fanta Orange", R.drawable.fantaorange, "Cocacola"));
        drinks.add(new Drink("Coca Cola",R.drawable.cocacola, "Cocacola"));
        drinks.add(new Drink("Sprite",R.drawable.sprite, "Cocacola"));
        drinks.add(new Drink("Fanta Passion",R.drawable.fantapassion, "Cocacola"));
        drinks.add(new Drink("Pepsi",R.drawable.pepsi, "Pepsi"));
        drinks.add(new Drink("RedBull ",R.drawable.redbull, "Redbull"));
        drinks.add(new Drink("Mountain Dew",R.drawable.mountaindew, "Pepsi"));
        drinks.add(new Drink("Monster",R.drawable.monster, "Monster Beverage Corparation"));
        return drinks;
    }
  private void onDrinkClick(Drink drink){
        Intent intent=new Intent(Drinks.this, Selection.class);
        startActivity(intent);

    }
}