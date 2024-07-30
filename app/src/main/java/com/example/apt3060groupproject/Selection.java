package com.example.apt3060groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Selection extends AppCompatActivity {
    ImageView drinkImage;
    TextView drinkName, priceTextView, quantity;
    Spinner sizeSpinner;
    Button add_to_cart, increase, decrease;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    // Declare variables for branch information
    private String selectedBranchId;
    private String selectedBranchName;

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

        auth = FirebaseAuth.getInstance();
        drinkImage = findViewById(R.id.drinkimage);
        drinkName = findViewById(R.id.drinkname);
        sizeSpinner = findViewById(R.id.spinner_size);
        priceTextView = findViewById(R.id.price);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        quantity = findViewById(R.id.quantity);
        add_to_cart = findViewById(R.id.addtocart);

        // Get the selected drink and branch details from the Intent
        Intent intent = getIntent();
        Drink selectedDrink = (Drink) intent.getSerializableExtra("DRINK");
        selectedBranchId = intent.getStringExtra("selectedBranchID");
        selectedBranchName = intent.getStringExtra("selectedBranchName");

        if (selectedDrink != null) {
            // Set drink image and name
            drinkImage.setImageResource(selectedDrink.getDrinkImage());
            drinkName.setText(selectedDrink.getDrinkName());

            // Set up size spinner
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

            // Set up quantity buttons
            increase.setOnClickListener(v -> {
                int currentQuantity = Integer.parseInt(quantity.getText().toString());
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
            });

            decrease.setOnClickListener(v -> {
                int currentQuantity = Integer.parseInt(quantity.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantity.setText(String.valueOf(currentQuantity));
                }
            });

            // Set up add to cart button
            add_to_cart.setOnClickListener(v -> {
                String name = selectedDrink.getDrinkName();
                int image = selectedDrink.getDrinkImage();
                String size = sizeSpinner.getSelectedItem().toString();
                int selected_quantity = Integer.parseInt(quantity.getText().toString());
                double price = selected_quantity * selectedDrink.getPriceForSize(size);

                // Create CartItem without user ID and branch ID
                CartItem cartItem = new CartItem(name, image, size, selected_quantity, price);

                // Set user ID if the user is logged in
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    cartItem.setUserId(user.getUid());
                }

                // Set branch ID
                cartItem.setBranchId(selectedBranchId);

                // Set a unique ID for the CartItem
                cartItem.setId(UUID.randomUUID().toString());

                // Ensure branch name and ID are available
                if (selectedBranchName != null && !selectedBranchName.isEmpty() && selectedBranchId != null && !selectedBranchId.isEmpty()) {
                    db = FirebaseFirestore.getInstance();
                    db.collection("branches")
                            .document(selectedBranchId) // Use the branch ID to identify the document
                            .collection("cart_items")
                            .add(cartItem)
                            .addOnSuccessListener(documentReference -> {
                                String id = documentReference.getId(); // Get the generated ID
                                cartItem.setId(id); // Set the ID to the CartItem object
                                Toast.makeText(Selection.this, "Added to Cart", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Selection.this, "Failed to add to cart", Toast.LENGTH_LONG).show();
                                Log.e("Selection", "Error adding to cart", e);
                            });
                } else {
                    Toast.makeText(Selection.this, "No branch selected", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
