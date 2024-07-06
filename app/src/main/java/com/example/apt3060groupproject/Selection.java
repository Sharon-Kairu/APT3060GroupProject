package com.example.apt3060groupproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Selection extends AppCompatActivity {
    ImageView drinkImage;
    TextView drinkName, priceTextView;

    Spinner sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);

        // Setup window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        drinkImage = findViewById(R.id.drinkimage);
        drinkName = findViewById(R.id.drinkname);
        sizeSpinner = findViewById(R.id.spinner_size);  // Corrected to match your layout XML
        priceTextView = findViewById(R.id.price);  // Renamed to follow Java naming conventions

        // Get the selected drink from the Intent
        Drink selectedDrink = (Drink) getIntent().getSerializableExtra("DRINK");
        if (selectedDrink != null) {
            // Set drink image and name
            drinkImage.setImageResource(selectedDrink.getDrinkImage());
            drinkName.setText(selectedDrink.getDrinkName());


            List<String> sizes = new ArrayList<>(selectedDrink.getSizePriceMap().keySet());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sizeSpinner.setAdapter(adapter);


            sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedSize = (String) parent.getItemAtPosition(position);
                    double price = selectedDrink.getPriceForSize(selectedSize);
                    String priceString = String.format(Locale.getDefault(), "%.2f", price);  // Format price to 2 decimal places
                    priceTextView.setText(priceString + " Shillings");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    priceTextView.setText("Select a size to see the price");
                }
            });
        }
    }
}
