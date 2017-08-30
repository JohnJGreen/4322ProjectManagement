package com.team11.cse4322.redalertuvnotifications;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
    /*
        This inner class acts as the background to the application. Using the URL provided, it'll go and grab the JSON and parse
        the JSON and return the uv index and uv alert
    */
public class UV_Lookup extends AsyncTask<String, String, String> {

    public AsyncResponse data = null;

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try{
            // sets up connection
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();

            // reads the json
            String line = "";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            // json string
            String finalJson = buffer.toString();

            // first the json array
            JSONArray parentArray = new JSONArray(finalJson);

            // the jsonobject inside the array
            JSONObject finalObject = parentArray.getJSONObject(0);

            // the key-value inside the json object
            int uvIndex = finalObject.getInt("UV_INDEX");
            int uvAlert = finalObject.getInt("UV_ALERT");

            // return info in order to display
            return "INDEX:"+ uvIndex + " ALERT:" + uvAlert;

            // exception handling
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                connection.disconnect(); // close connection
            }
            try{
                if(reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        data.processFinish(result); // display result upon finish
    }

}

