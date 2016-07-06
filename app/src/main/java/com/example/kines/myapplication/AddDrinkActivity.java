package com.example.kines.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kines on 2016-07-05.
 */

public class AddDrinkActivity extends ToolbarActivity {
    private ArrayList<Ingredient> ingredientsToAdd = new ArrayList<Ingredient>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        setToolbar();

        ((TextView)findViewById(R.id.toolbarText)).setText(R.string.addDrinkToolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner glassSelector = (Spinner) findViewById(R.id.addDrink_glass_spinner);
        String[] glassArray = new String[] {"Margarita", "Martini", "Highball"}; //should probably not be hard-coded - just testing
        ArrayAdapter<String> glassAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, glassArray);
        glassSelector.setAdapter(glassAdapter);

        Spinner ingredientSelector = (Spinner) findViewById(R.id.addDrink_addIngredient_spinner);
        String[] ingredientArray = new String[] {"Ing1", "Ing2", "Ing3"}; //should not be hard-coded - just testing
        ArrayAdapter<String> allIngredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ingredientArray);
        ingredientSelector.setAdapter(allIngredientAdapter);
        Button addIngredientBtn = (Button)findViewById(R.id.addDrink_addIngredientBtn);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.addDrink_addIngredientPopup_title);
                builder.setView(getLayoutInflater().inflate(R.layout.add_ingredient_layout, null));
                builder.setPositiveButton(R.string.addDrink_addIngredient_confirmBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //check that all fields are filled in and save in ingredientsToAdd, then close dialog
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.addDrink_addIngredient_cancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        });

        ListView ingredientListView = (ListView)findViewById(R.id.addDrink_addIngredientList);
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<Ingredient>(AddDrinkActivity.this, android.R.layout.simple_list_item_1, ingredientsToAdd);
        ingredientListView.setAdapter(ingredientAdapter);

        //Info to send to DB
        String drinkName = ((TextView)findViewById(R.id.addDrink_name)).getText().toString();
        String glassType = glassSelector.getSelectedItem().toString();
        String instructions = ((TextView)findViewById(R.id.addDrink_instructions)).getText().toString();
    }
}
