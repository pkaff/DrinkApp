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

        final Spinner glassSelector = (Spinner) findViewById(R.id.addDrink_glass_spinner);
        String[] glassArray = new String[] {"Margarita", "Martini", "Highball"}; //should probably not be hard-coded - just testing
        ArrayAdapter<String> glassAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, glassArray);
        glassSelector.setAdapter(glassAdapter);

        String[] ingredientArray = new String[] {"Ing1", "Ing2", "Ing3"}; //should not be hard-coded - just testing
        final ArrayAdapter<String> allIngredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ingredientArray);

        Button addIngredientBtn = (Button)findViewById(R.id.addDrink_addIngredientBtn);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.addDrink_addIngredientPopup_title);
                final View inflatedView = getLayoutInflater().inflate(R.layout.add_ingredient_layout, null);
                builder.setView(inflatedView);
                final Spinner ingredientSelector = (Spinner) inflatedView.findViewById(R.id.addDrink_addIngredient_spinner);
                ingredientSelector.setAdapter(allIngredientAdapter);
                builder.setPositiveButton(R.string.addDrink_addIngredient_confirmBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Check that all fields are filled in and save in ingredientsToAdd, then close dialog
                        Toast t = Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT);
                        TextView addDrink_ingredient_size = (TextView) findViewById(R.id.addDrink_addIngredient_size);
                        TextView addDrink_ingredient_unit = (TextView) findViewById(R.id.addDrink_addIngredient_unit);
                        if (textViewIsEmpty(addDrink_ingredient_size) || textViewIsEmpty(addDrink_ingredient_unit) || ingredientSelector.getSelectedItem() == null) {
                            //Form not filled out
                            t = Toast.makeText(v.getContext(), R.string.addDrink_addIngredient_error_incompleteForm, Toast.LENGTH_SHORT);
                        } else {
                            boolean errorCaught = false;
                            //Add ingredient
                            String ingredientName = ingredientSelector.getSelectedItem().toString();
                            String ingredientSizeString = addDrink_ingredient_size.getText().toString();
                            Double ingredientSize = 0.0;
                            try {
                                ingredientSize = Double.parseDouble(ingredientSizeString);
                            } catch (NumberFormatException e) {
                                errorCaught = true;
                                t = Toast.makeText(v.getContext(), R.string.addDrink_addIngredient_error_size, Toast.LENGTH_SHORT);
                            }
                            if (!errorCaught) {
                                String ingredientUnit = addDrink_ingredient_unit.getText().toString();
                                ingredientsToAdd.add(new Ingredient(ingredientName, ingredientSize, ingredientUnit));
                                allIngredientAdapter.notifyDataSetChanged();
                                t = Toast.makeText(v.getContext(), R.string.addDrink_addIngredient_success_ingAdded, Toast.LENGTH_SHORT);
                                dialog.dismiss();
                            }
                        }
                        t.show();
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
