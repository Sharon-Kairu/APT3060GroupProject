package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Drinks extends AppCompatActivity implements DrinksAdapter.OnDrinkClickListener {
    RecyclerView drinks;
    ImageButton cart;

    List<Drink> drinksList;
    DrinksAdapter drinksAdapter;

    // Declare variables for branch information
    private String selectedBranchId;
    private String selectedBranchName;
    private String userId;

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

        // Retrieve branch details from Intent extras
        Intent intent = getIntent();
        selectedBranchId = intent.getStringExtra("selectedBranchID");
        selectedBranchName = intent.getStringExtra("selectedBranchName");
        userId = intent.getStringExtra("USER_ID");


       TextView branchNameTextView = findViewById(R.id.branchNameTextview);
       branchNameTextView.setText(selectedBranchName);

        drinks = findViewById(R.id.drinks);
        drinks.setLayoutManager(new LinearLayoutManager(this));

        drinksList = getDrinksList();
        drinksAdapter = new DrinksAdapter(this, drinksList, this);
        drinks.setAdapter(drinksAdapter);

        cart = findViewById(R.id.cart);

        cart.setOnClickListener(v -> {
            Intent cartintent = new Intent(Drinks.this, Cart.class);
            cartintent.putExtra("userId", userId);
            cartintent.putExtra("selectedBranchID", selectedBranchId);
            startActivity(cartintent);
        });
    }

    public static List<Drink> getDrinksList() {
        List<Drink> drinks = new ArrayList<>();

        Drink fantaOrange = new Drink("Fanta Orange", R.drawable.fantaorange, "Cocacola");
        fantaOrange.addSizePrice("500ml", 80);
        fantaOrange.addSizePrice("1L", 120);
        fantaOrange.addSizePrice("2L", 200);
        drinks.add(fantaOrange);

        Drink cocaCola = new Drink("Coca Cola", R.drawable.cocacola, "Cocacola");
        cocaCola.addSizePrice("500ml", 80);
        cocaCola.addSizePrice("1L", 120);
        cocaCola.addSizePrice("2L", 200);
        drinks.add(cocaCola);

        Drink sprite = new Drink("Sprite", R.drawable.sprite, "Cocacola");
        sprite.addSizePrice("500ml", 80);
        sprite.addSizePrice("1L", 120);
        sprite.addSizePrice("2L", 200);
        drinks.add(sprite);

        Drink fantaPassion = new Drink("Fanta Passion", R.drawable.fantapassion, "Cocacola");
        fantaPassion.addSizePrice("500ml", 80);
        fantaPassion.addSizePrice("1L", 120);
        fantaPassion.addSizePrice("2L", 200);
        drinks.add(fantaPassion);

        Drink pepsi = new Drink("Pepsi", R.drawable.pepsi, "Pepsi");
        pepsi.addSizePrice("500ml", 60);
        pepsi.addSizePrice("1L", 110);
        pepsi.addSizePrice("2L", 180);
        drinks.add(pepsi);

        Drink redBull = new Drink("RedBull", R.drawable.redbull, "Redbull");
        redBull.addSizePrice("250ml", 230);
        redBull.addSizePrice("355ml", 370);
        redBull.addSizePrice("473ml", 450);
        drinks.add(redBull);

        Drink mountainDew = new Drink("Mountain Dew", R.drawable.mountaindew, "Pepsi");
        mountainDew.addSizePrice("500ml", 80);
        mountainDew.addSizePrice("1L", 120);
        mountainDew.addSizePrice("2L", 200);
        drinks.add(mountainDew);

        Drink monster = new Drink("Monster Energy Drink", R.drawable.monster, "Monster Beverage Corporation");
        monster.addSizePrice("355ml", 250);
        monster.addSizePrice("473ml", 400);
        monster.addSizePrice("710ml", 650);
        drinks.add(monster);

        return drinks;
    }

    @Override
    public void onDrinkClick(Drink drink) {
        // Create an intent to start the Selection activity
        Intent intent = new Intent(this, Selection.class);

        intent.putExtra("selectedBranchID", selectedBranchId); // Pass the branch ID
        intent.putExtra("selectedBranchName", selectedBranchName); // Pass the branch name
        intent.putExtra("DRINK", drink); // Pass the selected drink

        startActivity(intent);
    }
}
