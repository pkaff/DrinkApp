package com.example.kines.myapplication;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
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
    private int score;
    private int dbID;

    protected Drink(Parcel p) {
        name = p.readString();
        category = p.readString();
        glass = p.readString();
        ingredients = new ArrayList<Ingredient>();
        p.readList(ingredients, Ingredient.class.getClassLoader());
        instructions = p.readString();
    }

    public Drink(int id, String name, String glass, String instruction) {
        ingredients = new ArrayList<Ingredient>();
        this.dbID = id;
        this.name = name;
        this.glass = glass;
        this.instructions = instruction;
    }

    public boolean containsSomeOf(List<String> ingredientNames) {
        for (Ingredient i : ingredients) {
            if (ingredientNames.contains(i.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAllOf(List<String> ingredientNames) {
        for (String s : ingredientNames) {
            boolean contained = false;
            for (Ingredient i : ingredients) {
                if (i.getName().toLowerCase().equals(s)) {
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
            if (!ingredientNames.contains(i.getName().toLowerCase())) {
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

    public String getGlass() {
        return glass;
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
    }

    public static final Parcelable.Creator<Drink> CREATOR = new Parcelable.Creator<Drink>() {
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    public void addIngredients(ArrayList<Ingredient> ingredientsList) {
        this.ingredients.addAll(ingredientsList);
    }
}
