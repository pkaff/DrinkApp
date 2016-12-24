package com.example.kines.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.AddDrink.AddIngredientListener;
import com.example.kines.myapplication.AddDrink.OpenAddIngredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddDrinkActivity extends ToolbarActivity {
    private ArrayList<Ingredient> ingredientsToAdd = new ArrayList<Ingredient>();
    private Drink drink;

    @Override
    protected void onResume() {
        super.onResume();
    }

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Confirm button
        Button confirmDrinkBtn = (Button)findViewById(R.id.addDrink_confirmBtn);
        confirmDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject JSONDrink = formToJSON();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra(getString(R.string.addDrinkToMain), JSONDrink.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), R.string.addDrink_error_incompleteForm, Toast.LENGTH_SHORT);
                }
            }
        });

        //Cancel button
        Button cancelDrinkBtn = (Button)findViewById(R.id.addDrink_cancelBtn);
        cancelDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra(getString(R.string.addDrinkToMain), getString(R.string.addDrink_cancelled));
                startActivity(intent);
            }
        });

    }

    private JSONObject formToJSON() throws JSONException, IllegalArgumentException {
        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString().trim();
        String glassType = ((Spinner)findViewById(R.id.addDrink_glass_spinner)).getSelectedItem().toString().trim();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString().trim();

        //Throw illegal argument if not all required fields are filled in
        if (drinkName.isEmpty() || glassType.isEmpty() || instructions.isEmpty() || ingredientsToAdd.isEmpty()) {
            throw new IllegalArgumentException(getString(R.string.addDrink_error_incompleteForm));
        }

        drink = new Drink(drinkName, glassType, instructions, ingredientsToAdd);

        JSONObject JSONDrink = drink.toJSON();

        return JSONDrink;
    }

}
