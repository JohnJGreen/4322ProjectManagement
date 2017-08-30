package com.team11.cse4322.redalertuvnotifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationIntentService extends IntentService implements AsyncResponse {

    private static final int notifyID = 1;
    private static final String actionStart = "Action Start";
    private static final String actionStop = "Action Stop";

    public NotificationIntentService(){
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new  Intent(context, NotificationIntentService.class);
        intent.setAction(actionStart);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(actionStop);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (actionStart.equals(action)) {
                processStartNotification();
            }
            if (actionStop.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }

    }

    private void processDeleteNotification(Intent intent) {
        Log.d(getClass().getSimpleName(), "stopped");
    }

    private void processStartNotification() {
        // Do something here
        // data is stored it to a file

        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setContentTitle(getData())// hardcoded for now
                .setAutoCancel(true)
                .setContentText("low risk")// hardcorded for now
                .setSmallIcon(R.drawable.common_plus_signin_btn_text_light);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyID, new Intent(this, UV_Lookup.class), PendingIntent.FLAG_UPDATE_CURRENT);

        nBuilder.setContentIntent(pendingIntent);

        nBuilder.setDeleteIntent(AlertReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifyID, nBuilder.build());
    }

    private String getData() {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try{
            // sets up connection
            URL url = new URL("https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVDAILY/ZIP/76180/JSON");
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
            return "INDEX:"+ uvIndex;

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

    @Override
    public void processFinish(String output) {

    }
}
