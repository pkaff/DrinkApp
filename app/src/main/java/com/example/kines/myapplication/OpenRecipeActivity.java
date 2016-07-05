package com.example.kines.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class OpenRecipeActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);
        setToolbar();
        ((TextView)findViewById(R.id.toolbarText)).setText(R.string.openRecipeToolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Drink d = intent.getParcelableExtra(getString(R.string.mainToOpenRecipeActivityIntent));

        TextView rName = (TextView) findViewById(R.id.recipeName);
        TextView rGlass = (TextView) findViewById(R.id.glasType);
        TextView rIng = (TextView) findViewById(R.id.recipeIngredients);
        TextView rInstr = (TextView) findViewById(R.id.recipeInstructions);
        rName.setText(d.getFormattedName());
        rGlass.setText(getString(R.string.recipeGlassConstant) + d.getGlass());
        String ingredients = "";
        for (Ingredient i : d.getIngredients()) {
            if (i.getSize() != 0) {
                ingredients += i.getFormattedSize() + " ";
            }
            ingredients += i.getFormattedName() + "\n";
        }
        rIng.setText(ingredients);
        rInstr.setText(d.getInstructions());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(false);
        return result;
    }
}
