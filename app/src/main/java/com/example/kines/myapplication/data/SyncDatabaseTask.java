package com.example.kines.myapplication.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kines.myapplication.DatabaseHelper;
import com.example.kines.myapplication.Drink;
import com.example.kines.myapplication.Ingredient;
import com.example.kines.myapplication.MainActivity;
import com.example.kines.myapplication.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class SyncDatabaseTask extends AsyncTask<String, String, Void>
{
    private DatabaseHelper db;
    private ProgressDialog progressDialog;
    String result = "";
    private MainActivity mainActivity;

    public SyncDatabaseTask(MainActivity mainActivity, List<Drink> drinkList, Set<Ingredient> ingredientSet, DatabaseHelper myDb) {
        this.mainActivity = mainActivity;
        progressDialog = new ProgressDialog(mainActivity);
        this.db = myDb;
    }

    protected void onPreExecute() {
        Log.d("sync", "start pre excecute");

        if (!isNetworkAvailable()) {
            Toast.makeText(mainActivity, R.string.noInternetConnection, Toast.LENGTH_SHORT).show();
            cancel(true);
            return;
        }

        progressDialog.setMessage("Sync in progress");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                SyncDatabaseTask.this.cancel(true);
            }
        });
    }

    private boolean isNetworkAvailable() {
        //Make sure we have an internet connection
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.d("sync", "start post excecute");
        try {
            JSONArray jsonArray = new JSONArray(result);
            try {
                db.syncDrinks(jsonArray);
            } catch(JSONException f) {
                throw new RuntimeException(f);
            }
            mainActivity.populate();
            this.progressDialog.dismiss();
        } catch(JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.d("sync", "start in bg");
        URL url;
        try {
             url = new URL("http://danielsdrinks.azurewebsites.net/Drinks.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = IOUtils.toString(in, "utf-8");

                Log.d("sync", result);

            } finally {
                urlConnection.disconnect();
            }


        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
