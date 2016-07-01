package com.example.kines.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OpenRecipeActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);
        setToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Drink d = (Drink) intent.getParcelableExtra("ListViewClick");

        TextView rName = (TextView) findViewById(R.id.recipeName);
        TextView rGlass = (TextView) findViewById(R.id.glasType);
        TextView rIng = (TextView) findViewById(R.id.recipeIngredients);
        TextView rInstr = (TextView) findViewById(R.id.recipeInstructions);
        rName.setText(d.getName());
        rGlass.setText("Glass: " + d.getGlass());
        String ingredients = "";
        for (Ingredient i : d.getIngredients()) {
            if (i.getSize() != 0) {
                ingredients += i.getSize() + " ";
            }
            ingredients += i.getName() + "\n";
        }
        rIng.setText(ingredients);
        rInstr.setText(d.getInstructions());
    }
}
