package com.example.kines.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kines on 2016-04-18.
 */
public class Drink implements Parcelable, Comparable<Drink>{
    private String name;
    private String category;
    private String glass;
    private List<Ingredient> ingredients;
    private String instructions;
    private String dateModified;
    private int score;

    protected Drink(Parcel p) {
        name = p.readString();
        category = p.readString();
        glass = p.readString();
        ingredients = new ArrayList<Ingredient>();
        p.readList(ingredients, Ingredient.class.getClassLoader());
        instructions = p.readString();
        dateModified = p.readString();
    }

    public Drink(String name, String glass, String instructions, String dateModified) {
        this(name, glass, instructions);
        this.dateModified = dateModified;
    }

    public Drink(String name, String glass, String instructions, List<Ingredient> ingredients, String dateModified) {
        this(name, glass, instructions, ingredients);
        this.dateModified = dateModified;
    }

    public Drink(String name, String glass, String instructions, List<Ingredient> ingredients) {
        this(name, glass, instructions);
        addIngredients(ingredients);
    }

    public Drink(String name, String glass, String instruction) {
        ingredients = new ArrayList<Ingredient>();
        this.name = name;
        this.glass = glass;
        this.instructions = instruction;
        DateFormat format = new ConstantDateFormat();
        dateModified = format.format(new Date());
    }

    public boolean containsSomeOf(List<String> ingredientNames) {
        for (Ingredient i : ingredients) {
            if (ingredientNames.contains(i.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAllOf(List<String> ingredientNames) {
        for (String s : ingredientNames) {
            boolean contained = false;
            for (Ingredient i : ingredients) {
                if (i.getName().equals(s)) {
                    contained = true;
                    break;
                }
            }
            if (contained == false) {
                return false;
            }
        }
        return true;
    }

    public boolean canBeMadeWith(List<String> ingredientNames) {
        for (Ingredient i : ingredients) {
            if (!ingredientNames.contains(i.getName())) {
                return false;
            }
        }
        return true;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() { return instructions; }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return WordUtils.capitalize(name);
    }

    public String getGlass() {
        return WordUtils.capitalize(glass);
    }

    public boolean contains(String ingredient) {
        for (Ingredient i : ingredients) {
            if (i.is(ingredient)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Drink another) {
        return name.compareTo(another.name);
    }

    public void setScore(List<String> ingredients) {
        score = 0;
        for (String s : ingredients) {
            if (contains(s)) ++score;
        }
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) { return false; }
        if (this == other) { return true; }
        if (!(other instanceof Drink)) { return false; }
        return ((Drink)other).name.equalsIgnoreCase(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(glass);
        dest.writeList(ingredients);
        dest.writeString(instructions);
        dest.writeString(dateModified);
    }

    public static final Parcelable.Creator<Drink> CREATOR = new Parcelable.Creator<Drink>() {
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    public void addIngredients(List<Ingredient> ingredientsList) {
        this.ingredients.addAll(ingredientsList);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject o = new JSONObject();
        o.put("name", name);
        o.put("glass", glass);
        o.put("instructions", instructions);
        JSONArray a = new JSONArray();
        for (Ingredient i : ingredients) {
            a.put(i.toJSON());
        }
        o.put("ingredients", a);
        o.put("modified", dateModified);
        return o;
    }
}
