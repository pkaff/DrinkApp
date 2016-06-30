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

public class OpenRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);

        //Toolbar
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.showOverflowMenu();
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*findViewById(R.id.toolbarText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                String msg = "Clicked toolbar";
                intent.putExtra("Toolbar click", msg);
                startActivity(intent);
            }
        });*/

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
