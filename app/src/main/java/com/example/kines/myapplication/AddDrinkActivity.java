package com.example.kines.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kines on 2016-07-05.
 */

public class AddDrinkActivity extends ToolbarActivity {
    private List<Ingredient> ingredientsToAdd = new ArrayList<Ingredient>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        setToolbar();

        ((TextView)findViewById(R.id.toolbarText)).setText(R.string.addDrinkToolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner glassSelector = (Spinner) findViewById(R.id.addDrink_glass_spinner);
        String[] glassArray = new String[] {"Margarita", "Martini", "Highball"}; //should probably not be hard-coded - just testing
        ArrayAdapter<String> glassAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, glassArray);
        glassSelector.setAdapter(glassAdapter);

        Button addIngredientBtn = (Button)findViewById(R.id.addDrink_addIngredientBtn);
        ListView ingredientListView = (ListView)findViewById(R.id.addDrink_addIngredientList);
        //ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(this, );
        //ingredientListView.setAdapter();

        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString();
        String glassType = glassSelector.getSelectedItem().toString();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString();
    }
}
