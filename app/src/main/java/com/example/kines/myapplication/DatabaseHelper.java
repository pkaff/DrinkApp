package com.example.kines.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.kines.myapplication/databases/";
    private static String DB_NAME = "Drink_Recipes.db";

    private static String TABLE_DRINK_CREATE = "CREATE TABLE IF NOT EXISTS `drink` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `name` text NOT NULL,\n" +
            "  `glass` text NOT NULL,\n" +
            "  `instructions` text NOT NULL\n" +
            ")";

    private static String TABLE_DRINK_INGREDIENT_CREATE = "CREATE TABLE IF NOT EXISTS `drink_ingredient` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `drink_id` int(11) NOT NULL,\n" +
            "  `ingredient_id` int(11) NOT NULL,\n" +
            "  `size` double NOT NULL,\n" +
            "  `unit` varchar(20) NOT NULL\n" +
            ")";

    private static String TABLE_INGREDIENT_CREATE = "CREATE TABLE IF NOT EXISTS `ingredient` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `name` text NOT NULL\n" +
            ")";

    private MainActivity activity;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context, MainActivity activity) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.activity = activity;

        getWritableDatabase();
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_DRINK_CREATE);
        db.execSQL(TABLE_DRINK_INGREDIENT_CREATE);
        db.execSQL(TABLE_INGREDIENT_CREATE);

        this.myDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS drink");
        db.execSQL("DROP TABLE IF EXISTS drink_ingredients");
        db.execSQL("DROP TABLE IF EXISTS ingredients");

        onCreate(db);
    }

    public void queryAllDrinks(List<Drink> drinkList, Set<Ingredient> ingredients) throws SQLException {
        Cursor cursor = myDataBase.rawQuery("select * from Drinks", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Drink drink = new Drink(cursor);
                    for (Ingredient i : drink.getIngredients()) {
                        ingredients.add(i);
                    }
                    drinkList.add(drink);
                } while (cursor.moveToNext());
            }
        }
    }

    public void syncDrinks(JSONArray jsonArray) throws JSONException {
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject JSONdrink = jsonArray.getJSONObject(i);

            String glass = JSONdrink.getString("glass");
            int drinkId = JSONdrink.getInt("id");
            String instructions = JSONdrink.getString("instructions");
            String drinkName = JSONdrink.getString("name");

            // add drink to db

            JSONArray ingredients = JSONdrink.getJSONArray("ingredients");

            for(int j = 0; j < ingredients.length(); j++) {
                JSONObject JSONIngredient = ingredients.getJSONObject(j);

                int ingredientId = JSONIngredient.getInt("id");
                String ingredientName = JSONIngredient.getString("name");
                String unit = JSONIngredient.getString("unit");
                double size = JSONIngredient.getDouble("size");

                // add ingredient to db
                // add connection to db
            }
        }
    }
}