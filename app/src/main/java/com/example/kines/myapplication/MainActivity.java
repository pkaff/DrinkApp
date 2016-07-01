package com.example.kines.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kines.myapplication.data.SyncDatabaseTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    List<Drink> drinkList = new ArrayList<Drink>();
    Set<Ingredient> ingredientSet = new TreeSet<Ingredient>();
    SearchableAdapter adapter;
    ListView lv;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //toolbar
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        //Database stuff
        myDb = new DatabaseHelper(this, this);
        AsyncTask task = new SyncDatabaseTask(this, drinkList, ingredientSet, myDb).execute();

        populate();

        edit = (EditText) findViewById(R.id.drinkFilterText);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getNameFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void populate() {
        Collections.sort(drinkList); //Sort by name

        adapter = new SearchableAdapter(MainActivity.this, drinkList);

        //listview
        lv = (ListView) findViewById(R.id.drinkListView);
        lv.setAdapter(adapter);
        lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), OpenRecipeActivity.class);
                String msg = "";
                if (view instanceof  TextView) {
                    msg = ((TextView) view).getText().toString();
                } else if (view instanceof RelativeLayout) {
                    msg = ((TextView)((RelativeLayout) view).getChildAt(0)).getText().toString();
                } else if (view instanceof ImageView) {
                    ((TextView)(((RelativeLayout)(view.getParent())).getChildAt(0))).getText().toString();

                }
                intent.putExtra("ListViewClick", msg);
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();

        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.ingredientSelector);
        multiSpinner.setItems(ingredientSet, "Ingredients filtering", new MSL(), adapter);
    }

    public class MSL implements MultiSpinner.MultiSpinnerListener{

        @Override
        public void onItemsSelected(boolean[] selected) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
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

        switch(id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
