package com.example.kines.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kines.myapplication.data.SyncDatabaseTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends ToolbarActivity {

    DatabaseHelper myDb;
    List<Drink> drinkList = new ArrayList<Drink>();
    Set<Ingredient> ingredientSet = new TreeSet<Ingredient>();
    SearchableAdapter adapter;
    ListView lv;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        createSearchAction(menu);
        return result;
    }

    private void createSearchAction(final Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.searchViewHint));
        //Filter when changing text in searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getNameFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_sync:
                createSyncAction();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSyncAction() {
        //Make sure we have an internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            new SyncDatabaseTask(this, drinkList, ingredientSet, myDb).execute();
        } else {
            Toast.makeText(this, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.firstOpen), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.firstOpen), Boolean.TRUE);
            edit.commit();
            new SyncDatabaseTask(this, drinkList, ingredientSet, myDb).execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        setToolbar();

        //Setting default values to settings
        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);

        //Database stuff
        myDb = new DatabaseHelper(this, this);

        populate();
    }

    public void populate() {
        myDb.populateDrinks(drinkList, ingredientSet);
        Collections.sort(drinkList); //Sort by name

        adapter = new SearchableAdapter(MainActivity.this, drinkList);

        //Drink listview
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
                intent.putExtra(getString(R.string.mainToOpenRecipeActivityIntent), msg);
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();

        //Filtering spinner
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.ingredientSelector);
        multiSpinner.setItems(ingredientSet, getString(R.string.multiSpinnerTitle), new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }, adapter);
        multiSpinner.setLongClickable(true);
        multiSpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                String hint = prefs.getString(getString(R.string.filterModePrefKey), getString(R.string.multiSpinnerHintDefault));
                Toast.makeText(v.getContext(), hint, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }
}
