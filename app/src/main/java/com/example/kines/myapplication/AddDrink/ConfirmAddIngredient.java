package com.example.kines.myapplication.AddDrink;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import com.example.kines.myapplication.AddDrinkActivity;
import com.example.kines.myapplication.Ingredient;
import com.example.kines.myapplication.R;

import java.util.ArrayList;

import static com.example.kines.myapplication.R.id.ingredientSelector;

public class ConfirmAddIngredient implements DialogInterface.OnClickListener {

    private AddDrinkActivity activity;
    private View view;
    private Spinner ingredientSelector;
    private ArrayList<Ingredient> ingredientsToAdd;
    private ArrayAdapter<Ingredient> ingredientsToAddAdapter;

    public ConfirmAddIngredient(AddDrinkActivity activity, View view, Spinner ingredientSelector, ArrayList<Ingredient> ingredientsToAdd, ArrayAdapter<Ingredient> ingredientsToAddAdapter) {
        this.activity = activity;
        this.view = view;
        this.ingredientSelector = ingredientSelector;
        this.ingredientsToAdd = ingredientsToAdd;
        this.ingredientsToAddAdapter = ingredientsToAddAdapter;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //Check that all fields are filled in and save in ingredientsToAdd, then close dialog
        Toast t = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);
        TextView addDrink_ingredient_size = (TextView) view.findViewById(R.id.addDrink_addIngredient_size);
        TextView addDrink_ingredient_unit = (TextView) view.findViewById(R.id.addDrink_addIngredient_unit);
        if (textViewIsEmpty(addDrink_ingredient_size) || textViewIsEmpty(addDrink_ingredient_unit) || ingredientSelector.getSelectedItem() == null) {
            //Form not filled out
            t = Toast.makeText(view.getContext(), R.string.addDrink_error_incompleteForm, Toast.LENGTH_SHORT);
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
            String ingredientUnit = addDrink_ingredient_unit.getText().toString();
            Ingredient ingredientToAdd = new Ingredient(ingredientName, ingredientSize, ingredientUnit);
            if (ingredientsToAdd.contains(ingredientToAdd)) {
                errorCaught = true;
                t = Toast.makeText(view.getContext(), R.string.addDrink_addIngredient_error_duplicatedIngredient, Toast.LENGTH_SHORT);
            }
            if (!errorCaught) {
                ingredientsToAdd.add(ingredientToAdd);
                t = Toast.makeText(view.getContext(), R.string.addDrink_addIngredient_success_ingAdded, Toast.LENGTH_SHORT);
                dialog.dismiss();
                ingredientsToAddAdapter.notifyDataSetChanged();
            }
        }
        t.show();
    }
    private boolean textViewIsEmpty(TextView v) {
        return v.getText().toString().trim().length() == 0;
    }
}
