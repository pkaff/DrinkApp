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
    private static final String DB_PATH = "/data/data/com.example.kines.myapplication/databases/";
    private static final String DB_NAME = "Drink_Recipes.db";

    private static final String TABLE_DRINK_CREATE = "CREATE TABLE IF NOT EXISTS `drink` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `name` text NOT NULL,\n" +
            "  `glass` text NOT NULL,\n" +
            "  `instructions` text NOT NULL\n" +
            ")";

    private static final String TABLE_DRINK_INGREDIENT_CREATE = "CREATE TABLE IF NOT EXISTS `drink_ingredient` (\n" +
            "  `drink_id` int(11) NOT NULL,\n" +
            "  `ingredient_id` int(11) NOT NULL,\n" +
            "  `size` double NOT NULL,\n" +
            "  `unit` varchar(20) NOT NULL\n" +
            ")";

    private static final String TABLE_INGREDIENT_CREATE = "CREATE TABLE IF NOT EXISTS `ingredient` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `name` text NOT NULL\n" +
            ")";

    private static final String TABLE_DRINK_DROP = "DROP TABLE IF EXISTS drink";
    private static final String TABLE_DRINK_INGREDIENT_DROP = "DROP TABLE IF EXISTS drink_ingredient";
    private static final String TABLE_INGREDIENT_DROP = "DROP TABLE IF EXISTS ingredient";

    private MainActivity activity;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context, MainActivity activity) {
        super(context, DB_NAME, null, 2);
        this.myContext = context;
        this.activity = activity;

        this.myDataBase = getWritableDatabase();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DRINK_DROP);
        db.execSQL(TABLE_DRINK_INGREDIENT_DROP);
        db.execSQL(TABLE_INGREDIENT_DROP);

        onCreate(db);
    }

    public void populateDrinks(List<Drink> drinkList, Set<Ingredient> ingredients) throws SQLException {
        //Clear drinks and ingredients to prepare fetching new ones from the database
        if (!drinkList.isEmpty()) {
            drinkList.clear();
        }
        if (!ingredients.isEmpty()) {
            ingredients.clear();
        }
        // fetch all drinks
        Cursor cursor = myDataBase.rawQuery("select id, name, glass, instructions from drink", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                do {
                    int dbID = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String glass = cursor.getString(2);
                    String instructions = cursor.getString(3);

                    Drink drink = new Drink(dbID, name, glass, instructions);

                    ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();

                    // fetch ingredients from DB
                    Cursor ingredientCursor = myDataBase.rawQuery("SELECT\n" +
                            "ingredient.id,\n" +
                            "ingredient.name,\n" +
                            "drink_ingredient.size,\n" +
                            "drink_ingredient.unit\n" +
                            "FROM\n" +
                            "drink_ingredient\n" +
                            "INNER JOIN ingredient ON ingredient.id = drink_ingredient.ingredient_id\n" +
                            "WHERE \n" +
                            "drink_ingredient.drink_id =" + dbID, null);

                    if(ingredientCursor != null) {
                        if(ingredientCursor.moveToFirst()) {
                            do {
                                //int ingredientID = ingredientCursor.getInt(0);
                                String ingredientName = ingredientCursor.getString(1);
                                double ingredientSize = ingredientCursor.getDouble(2);
                                String ingredientUnit = ingredientCursor.getString(3);

                                Ingredient ingredient = new Ingredient(ingredientName, ingredientSize, ingredientUnit);
                                ingredientsList.add(ingredient);
                                ingredients.add(ingredient);
                            } while(ingredientCursor.moveToNext());
                        }
                    }

                    drink.addIngredients(ingredientsList);

                    drinkList.add(drink);
                } while (cursor.moveToNext());

            }
        }
    }

    public void syncDrinks(JSONArray jsonArray) throws JSONException {
        //onUpgrade(myDataBase, 0, 0);
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject JSONdrink = jsonArray.getJSONObject(i);

            String glass = JSONdrink.getString("glass");
            int drinkId = JSONdrink.getInt("id");
            String instructions = JSONdrink.getString("instructions");
            String drinkName = JSONdrink.getString("name");
            String modifiedDate = JSONdrink.getString("modified");

            ContentValues values = new ContentValues();
            values.put("id", drinkId);
            values.put("name", drinkName);
            values.put("instructions", instructions);
            values.put("glass", glass);
            values.put("modified", modifiedDate);

            myDataBase.insert("drink", null, values);

            JSONArray ingredients = JSONdrink.getJSONArray("ingredients");

            for(int j = 0; j < ingredients.length(); j++) {
                JSONObject JSONIngredient = ingredients.getJSONObject(j);

                int ingredientId = JSONIngredient.getInt("id");
                String ingredientName = JSONIngredient.getString("name");
                String unit = JSONIngredient.getString("unit");
                double size = JSONIngredient.getDouble("size");

                // add ingredient to db
                Cursor checkIngredient = myDataBase.rawQuery("SELECT * FROM ingredient WHERE id = " + ingredientId,null);

                if(checkIngredient.getCount() == 0) {
                    values = new ContentValues();
                    values.put("id", ingredientId);
                    values.put("name", ingredientName);
                    myDataBase.insert("ingredient", null, values);
                }

                // add connection to db
                values = new ContentValues();
                values.put("ingredient_id", ingredientId);
                values.put("drink_id", drinkId);
                values.put("size", size);
                values.put("unit", unit);

                myDataBase.insert("drink_ingredient", null, values);

            }
        }
    }
}