package com.example.kines.myapplication.AddDrink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.AddDrinkActivity;
import com.example.kines.myapplication.Ingredient;
import com.example.kines.myapplication.R;

import java.util.ArrayList;

public class AddIngredientListener implements View.OnClickListener {

    private AddDrinkActivity activity;
    private ArrayAdapter<String> allIngredientAdapter;
    private ArrayList<Ingredient> ingredientsToAdd;

    public AddIngredientListener(AddDrinkActivity activity, ArrayAdapter<String> allIngredientAdapter, ArrayList<Ingredient> ingredientsToAdd) {
        this.activity = activity;
        this.allIngredientAdapter = allIngredientAdapter;
        this.ingredientsToAdd = ingredientsToAdd;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.addDrink_addIngredientPopup_title);
        final View inflatedView = activity.getLayoutInflater().inflate(R.layout.add_ingredient_layout, null);
        builder.setView(inflatedView);
        final Spinner ingredientSelector = (Spinner) inflatedView.findViewById(R.id.addDrink_addIngredient_spinner);
        ingredientSelector.setAdapter(allIngredientAdapter);
        builder.setPositiveButton(R.string.addDrink_addIngredient_confirmBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check that all fields are filled in and save in ingredientsToAdd, then close dialog
                Toast t = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);
                TextView addDrink_ingredient_size = (TextView) activity.findViewById(R.id.addDrink_addIngredient_size);
                TextView addDrink_ingredient_unit = (TextView) activity.findViewById(R.id.addDrink_addIngredient_unit);
                if (textViewIsEmpty(addDrink_ingredient_size) || textViewIsEmpty(addDrink_ingredient_unit) || ingredientSelector.getSelectedItem() == null) {
                    //Form not filled out
                    t = Toast.makeText(view.getContext(), R.string.addDrink_addIngredient_error_incompleteForm, Toast.LENGTH_SHORT);
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
                        t = Toast.makeText(view.getContext(), R.string.addDrink_addIngredient_error_size, Toast.LENGTH_SHORT);
                    }
                    if (!errorCaught) {
                        String ingredientUnit = addDrink_ingredient_unit.getText().toString();
                        ingredientsToAdd.add(new Ingredient(ingredientName, ingredientSize, ingredientUnit));
                        allIngredientAdapter.notifyDataSetChanged();
                        t = Toast.makeText(view.getContext(), R.string.addDrink_addIngredient_success_ingAdded, Toast.LENGTH_SHORT);
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

    private boolean textViewIsEmpty(TextView v) {
        return v.getText().toString().trim().length() == 0;
    }
}

