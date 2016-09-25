package com.example.kines.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Kines on 2016-04-18.
 */
public class Ingredient implements Parcelable, Comparable {
    private String name;
    private double size;
    private String unit;

    protected Ingredient(Parcel p) {
        name = p.readString();
        size = p.readDouble();
        unit = p.readString();
    }
    public Ingredient(String name, double size, String unit) {
        this.name = name;
        this.size = size;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public String getFormattedSize() {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(size) + " " + unit;
    }

    public boolean is(String ingredient) {
        return name.equalsIgnoreCase(ingredient);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Ingredient)) return false;
        Ingredient other = (Ingredient)obj;
        return (other.name.equalsIgnoreCase(this.name)) ? true : false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(size);
        dest.writeString(unit);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int compareTo(Object another) {
        return name.compareTo(((Ingredient)another).name);
    }

    public String getFormattedName() {
        return WordUtils.capitalize(getName());
    }

    @Override
    public String toString() {
        return size + " " + unit + " " + name;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("name", name);
        o.put("size", size);
        o.put("unit", unit);
        return o;
    }
}
