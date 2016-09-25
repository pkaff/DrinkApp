package com.example.kines.myapplication.data;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Superdator on 2016-09-25.
 */

public class AddDrinkTask extends AsyncTask<String, String, String> {

        public AddDrinkTask(){
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground() {
            String urlString = "http://danielsdrinks.joyo.se/AddDrink.php"; // URL to call
            String resultToDisplay = "";

            InputStream in = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                OutputStream outputStream = urlConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                

                writer.write("");

                // excecute the request
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            try {
                resultToDisplay = IOUtils.toString(in, "UTF-8");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultToDisplay;
        }


        @Override
        protected void onPostExecute(String result) {
            //Update the UI
        }
        Older Answer

        Note: This solution is outdated. It only works on Android devices up to 5.1. Android 6.0 and above do not include the Apache http client used in this answer.

                Http Client from Apache Commons is the way to go. It is already included in android. Here's a simple example of how to do HTTP Post using it.

        public void postData() {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", "12345"));
                nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
}
