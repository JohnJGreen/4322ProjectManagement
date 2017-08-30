package com.team11.cse4322.redalertuvnotifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlertReceiver extends BroadcastReceiver implements AsyncResponse{

    private static final String actionStartNotification = "Start_Notification";
    private static final String actionStopNotification = "Stop Notification";
    private static final int intervalHours = 1;

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerAt(new Date()), intervalHours * 10000, alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Intent serviceIntent = null;
        if (actionStartNotification.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
        } else if (actionStopNotification.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }

    private static long getTriggerAt(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_HOURS);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.setAction(actionStartNotification);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.setAction(actionStopNotification);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void processFinish(String output) {

    }
}
