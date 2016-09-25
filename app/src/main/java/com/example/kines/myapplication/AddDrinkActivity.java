package com.example.kines.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.AddDrink.AddIngredientListener;
import com.example.kines.myapplication.AddDrink.OpenAddIngredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddDrinkActivity extends ToolbarActivity {
    private ArrayList<Ingredient> ingredientsToAdd = new ArrayList<Ingredient>();
    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        setToolbar();

        ((TextView)findViewById(R.id.toolbarText)).setText(R.string.addDrinkToolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Spinner glassSelector = (Spinner) findViewById(R.id.addDrink_glass_spinner);
        String[] glassArray = new String[] {"Margarita", "Martini", "Highball"}; //should probably not be hard-coded - just testing
        ArrayAdapter<String> glassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, glassArray);
        glassSelector.setAdapter(glassAdapter);

        String[] ingredientArray = new String[] {"Ing1", "Ing2", "Ing3"}; //should not be hard-coded - just testing
        final ArrayAdapter<String> allIngredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredientArray);

        // Add ingredient
        Button addIngredientBtn = (Button)findViewById(R.id.addDrink_addIngredientBtn);
        addIngredientBtn.setOnClickListener(new AddIngredientListener(this, allIngredientAdapter, ingredientsToAdd));
        addIngredientBtn.setOnClickListener(new OpenAddIngredient(this, allIngredientAdapter, ingredientsToAdd));

        ListView ingredientListView = (ListView)findViewById(R.id.addDrink_addIngredientList);
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<Ingredient>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, ingredientsToAdd);
        ingredientListView.setAdapter(ingredientAdapter);

        //Confirm button
        Button confirmDrinkBtn = (Button)findViewById(R.id.addDrink_confirmBtn);
        confirmDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra(getString(R.string.addDrinkToMainConfirm), drink);
                startActivity(intent);
            }
        });

        //Cancel button
        Button cancelDrinkBtn = (Button)findViewById(R.id.addDrink_confirmBtn);
        cancelDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddDrinkActivity.class);
                intent.putExtra(getString(R.string.addDrinkToMainCancel), "");
                startActivity(intent);
            }
        });

    }

    private boolean textViewIsEmpty(TextView v) {
        return v.getText().toString().trim().length() == 0;
    }

    private JSONObject postDrink() throws JSONException {
        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString();
        String glassType = ((Spinner)findViewById(R.id.addDrink_glass_spinner)).getSelectedItem().toString();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString();

        drink = new Drink(drinkName, glassType, instructions, ingredientsToAdd);

        JSONObject JSONDrink = new JSONObject();
        JSONDrink.put("Drink", drink.toJSON());
        return JSONDrink;
    }

}
