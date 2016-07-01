package com.example.kines.myapplication.data;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kines.myapplication.MainActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncDatabaseTask extends AsyncTask<String, String, Void>
{
    private ProgressDialog progressDialog;
    String result = "";

    public SyncDatabaseTask(MainActivity mainActivity) {
        progressDialog = new ProgressDialog(mainActivity);
    }

    protected void onPreExecute() {
        Log.d("sync", "start pre excecute");
        progressDialog.setMessage("Sync in progress");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                SyncDatabaseTask.this.cancel(true);
            }
        });
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.d("sync", "start post excecute");
        try {
            JSONArray jsonArray = new JSONArray(result);


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
             url = new URL("http://danielsdrinks.joyo.se/Drinks.php");
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
