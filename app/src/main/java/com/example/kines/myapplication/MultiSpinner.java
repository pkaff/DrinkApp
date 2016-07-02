package com.example.kines.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiSpinner extends Spinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> ingredients;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private SearchableAdapter adapter;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //Filter according to selections
        ArrayAdapter<String> a = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_layout,
                new String[] { defaultText });
        setAdapter(a);
        listener.onItemsSelected(selected);
        List<String> selectedIngredients = new ArrayList<String>();
        for (int i = 0; i < selected.length; ++i) {
            if (selected[i]) {
                selectedIngredients.add(ingredients.get(i));
            }
        }
        String filterString = "";
        for (String s : selectedIngredients) {
            filterString += s;
            filterString += ";";
        }
        adapter.getIngredientFilter().filter(filterString);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                ingredients.toArray(new CharSequence[ingredients.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(Set<Ingredient> items, String allText,
                         MultiSpinnerListener listener, SearchableAdapter a) {
        this.ingredients = new ArrayList<String>();
        for (Ingredient i : items) {
            ingredients.add(i.getName());
        }
        this.defaultText = allText;
        this.listener = listener;
        this.adapter = a;

        // all selected by default
        selected = new boolean[ingredients.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_layout, new String[] { allText });
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}