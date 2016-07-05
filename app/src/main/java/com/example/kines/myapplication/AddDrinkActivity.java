package com.example.kines.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Kines on 2016-07-05.
 */

public class AddDrinkActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        setToolbar();

        ((TextView)findViewById(R.id.toolbarText)).setText(R.string.addDrinkToolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
