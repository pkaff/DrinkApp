package com.example.kines.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.AddDrink.OpenAddIngredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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

        Intent intent = getIntent();
        ArrayList<String> ingredients = intent.getStringArrayListExtra(getString(R.string.mainToAddDrinkActivity_ingredients));
        ArrayList<String> glasses = intent.getStringArrayListExtra(getString(R.string.mainToAddDrinkActivity_glasses));

        final Spinner glassSelector = (Spinner) findViewById(R.id.addDrink_glass_spinner);
        String[] glassArray = new String[glasses.size()];
        for (int k = 0; k < glasses.size(); ++k) {
            glassArray[k] = glasses.get(k);
        }
        ArrayAdapter<String> glassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, glassArray);
        glassSelector.setAdapter(glassAdapter);

        //Add all ingredient names to the string array
        String[] ingredientArray = new String[ingredients.size()];
        for (int k = 0; k < ingredients.size(); ++k) {
            ingredientArray[k] = ingredients.get(k);
        }
        final ArrayAdapter<String> allIngredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredientArray);

        ListView ingredientListView = (ListView)findViewById(R.id.addDrink_addIngredientList);
        final ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<Ingredient>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, ingredientsToAdd);
        ingredientListView.setAdapter(ingredientAdapter);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingredientsToAdd.remove(position);
                ingredientAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), R.string.addDrink_removedIngredientFromList, Toast.LENGTH_SHORT).show();
            }
        });

        // Add ingredient
        Button addIngredientBtn = (Button)findViewById(R.id.addDrink_addIngredientBtn);
        addIngredientBtn.setOnClickListener(new OpenAddIngredient(this, allIngredientAdapter, ingredientAdapter, ingredientsToAdd));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Confirm button
        Button confirmDrinkBtn = (Button)findViewById(R.id.addDrink_confirmBtn);
        confirmDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drink = formToDrink();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra(getString(R.string.addDrinkToMain), drink);
                    startActivity(intent);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), R.string.addDrink_error_incompleteForm, Toast.LENGTH_SHORT).show();
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

    private Drink formToDrink() throws IllegalArgumentException {
        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString().trim();
        String glassType = ((Spinner)findViewById(R.id.addDrink_glass_spinner)).getSelectedItem().toString().trim();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString().trim();

        //Throw illegal argument if not all required fields are filled in
        if (drinkName.isEmpty() || glassType.isEmpty() || instructions.isEmpty() || ingredientsToAdd.isEmpty()) {
            throw new IllegalArgumentException(getString(R.string.addDrink_error_incompleteForm));
        }

        return new Drink(drinkName, glassType, instructions, ingredientsToAdd);
    }

}
