package com.example.kines.myapplication.AddDrink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.AddDrinkActivity;
import com.example.kines.myapplication.Ingredient;
import com.example.kines.myapplication.R;

import java.util.ArrayList;

public class OpenAddIngredient implements View.OnClickListener {

    private AddDrinkActivity activity;
    private ArrayAdapter<String> allIngredientAdapter;
    private ArrayAdapter<Ingredient> ingredientsToAddAdapter;
    private ArrayList<Ingredient> ingredientsToAdd;

    public OpenAddIngredient(AddDrinkActivity activity, ArrayAdapter<String> allIngredientAdapter, ArrayAdapter<Ingredient> ingredientsToAddAdapter, ArrayList<Ingredient> ingredientsToAdd) {
        this.activity = activity;
        this.allIngredientAdapter = allIngredientAdapter;
        this.ingredientsToAdd = ingredientsToAdd;
        this.ingredientsToAddAdapter = ingredientsToAddAdapter;
    }

    @Override
    public void onClick(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.addDrink_addIngredientPopup_title);
        final View inflatedView = activity.getLayoutInflater().inflate(R.layout.add_ingredient_layout, null);
        builder.setView(inflatedView);
        final Spinner ingredientSelector = (Spinner) inflatedView.findViewById(R.id.addDrink_addIngredient_spinner);
        ingredientSelector.setAdapter(allIngredientAdapter);
        builder.setPositiveButton(R.string.addDrink_addIngredient_confirmBtn, new ConfirmAddIngredient(activity, inflatedView, ingredientSelector, ingredientsToAdd, ingredientsToAddAdapter));
        builder.setNegativeButton(R.string.addDrink_addIngredient_cancelBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean textViewIsEmpty(TextView v) {
        return v.getText().toString().trim().length() == 0;
    }
}