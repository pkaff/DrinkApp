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

import java.util.ArrayList;
import java.util.List;


public class AddDrinkActivity extends ToolbarActivity {
    private ArrayList<Ingredient> ingredientsToAdd = new ArrayList<Ingredient>();
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
    }

    private boolean textViewIsEmpty(TextView v) {
        return v.getText().toString().trim().length() == 0;
    }

    private boolean postDrink() {
        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString();
        String glassType = ((Spinner)findViewById(R.id.addDrink_glass_spinner)).getSelectedItem().toString();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString();
        return true;
    }

}
