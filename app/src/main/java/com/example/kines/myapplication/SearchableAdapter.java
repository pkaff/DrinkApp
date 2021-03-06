package com.example.kines.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kines on 2016-04-18.
 */
public class SearchableAdapter extends BaseAdapter {
    private List<Drink> originalData = null;
    private List<Drink> filteredData = null;
    private LayoutInflater mInflater;
    private IngredientFilter iFilter;
    private NameFilter nFilter;
    private Context context;

    public SearchableAdapter(Context context, List<Drink> data) {
        this.filteredData = data;
        this.originalData = data;
        mInflater = LayoutInflater.from(context);
        iFilter = new IngredientFilter();
        nFilter = new NameFilter();
        this.context = context;
    }
    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredData.indexOf(filteredData.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_layout, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.drinkItem);
            //holder.img = (ImageView) convertView.findViewById(R.id.drinkImage);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(filteredData.get(position).getFormattedName());
        holder.text.setInputType(InputType.TYPE_NULL);
        holder.text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String drinkName = ((TextView) v).getText().toString().toLowerCase();
                for (Drink d : filteredData) {
                    if (d.getName().equals(drinkName)) {
                        Intent intent = new Intent(v.getContext(), OpenRecipeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(context.getString(R.string.mainToOpenRecipeActivityIntent), d);
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView img;
    }

    public Filter getNameFilter() {
        return nFilter;
    }

    public Filter getIngredientFilter() {
        return iFilter;
    }

    protected class IngredientFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Drink> list = originalData;

            int count = list.size();
            ArrayList<Drink> nList = new ArrayList<>(count);

            Drink filterableDrink;
            List<String> ingredientsInFilter = Arrays.asList(filterString.split(";"));

            if (filterString.length() == 0) {
                results.values = originalData;
                results.count = originalData.size();
                return results;
            }

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            String mode = pref.getString(context.getString(R.string.filterModePrefKey), context.getString(R.string.multiSpinnerHintDefault));
            String errorMessage;
            switch (mode) {
                case "And mode":
                    for (int i = 0; i < count; ++i) {
                        filterableDrink = list.get(i);
                        if (filterableDrink.containsAllOf(ingredientsInFilter)) {
                            nList.add(filterableDrink);
                        }
                    }
                    errorMessage = "No drinks in the database contains all of the ingredients selected in the ingredient selector.";
                    break;
                case "Ingredient mode":
                    for (int i = 0; i < count; ++i) {
                        filterableDrink = list.get(i);
                        if (filterableDrink.canBeMadeWith(ingredientsInFilter)) {
                            nList.add(filterableDrink);
                        }
                    }
                    errorMessage = "No drinks in the database can be made with the ingredients selected in the ingredient selector.";
                    break;
                case "Or mode":
                    for (int i = 0; i < count; ++i) {
                        filterableDrink = list.get(i);
                        if (filterableDrink.containsSomeOf(ingredientsInFilter)) {
                            nList.add(filterableDrink);
                        }
                    }

                default:
                    errorMessage = "No filter type selected in settings.";
                    break;
            }

            //Sorting - not working
            /*for (int i = 0; i < count; ++i) {
                filterableDrink = list.get(i);
                filterableDrink.setScore(ingredientsFilter);
                //if (filterableDrink.containsSomeOf(ingredientsFilter)) {
                nList.add(filterableDrink);
                //}
            }
            Collections.sort(nList, new Comparator<Drink>() {
                @Override
                public int compare(Drink lhs, Drink rhs) {
                    return rhs.getScore() - lhs.getScore();
                }
            });*/

            if (nList.isEmpty()) {
                new AlertDialog.Builder(context)
                        .setTitle("Bad filter")
                        .setMessage(errorMessage)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                results.values = originalData;
                results.count = originalData.size();
            } else {
                results.values = nList;
                results.count = nList.size();
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                filteredData = (List<Drink>)results.values;
                notifyDataSetChanged();
            }

        }
    }

    protected class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Drink> list = originalData;

            int count = list.size();
            final ArrayList<Drink> nList = new ArrayList<>(count);

            Drink filterableDrink;

            for (int i = 0; i < count; ++i) {
                filterableDrink = list.get(i);
                if (filterableDrink.getName().toLowerCase().contains(filterString)) {
                    nList.add(filterableDrink);
                }
            }
            if (nList.size() == 0){
                results.values = originalData;
                results.count = originalData.size();
            } else {
                results.values = nList;
                results.count = nList.size();
            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                filteredData = (List<Drink>)results.values;
                notifyDataSetChanged();
            }

        }
    }
}
