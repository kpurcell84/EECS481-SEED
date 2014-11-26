package edu.umich.seedforandroid.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.util.AlertsManager;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class GcmBroadcastReceiver extends BroadcastReceiver  {

    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    private static final String PRIORITY = "priority";
    private static final String PATIENT_EMAIL = "patient_email";
    private static final String PATIENT_NAME = "patient_name";


    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)  {

        Bundle extras = intent.getExtras();

        String priority = extras.getString(PRIORITY);
        String email = extras.getString(PATIENT_EMAIL);
        String name = extras.getString(PATIENT_NAME);

        SharedPrefsUtil prefsUtil = new SharedPrefsUtil(context);
        String userType = prefsUtil.getUserAccountType("");
        if (!userType.equals("") && !userType.equals(SharedPrefsUtil.ACCOUNT_TYPE_NONE)) {

            String contentTitle;
            String contentBody;
            if (priority.equals(AlertsManager.PRIORITY_EARLY)) {

                contentTitle = context.getString(R.string.notif_header_early);
                contentBody = AlertsManager.buildEarlyAlertString(context, name);
            }
            else {
                contentTitle = context.getString(R.string.notif_header_emergency);
                if (userType.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR)) {

                    contentBody = AlertsManager.buildDoctorEmergencyAlertString(context, name);
                }
                else {

                    contentBody = AlertsManager.buildPatientEmergencyAlertString(context);
                }
            }

            int notifId = Calendar.getInstance().get(Calendar.MILLISECOND);
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, notifId, launchIntent, 0);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(contentTitle)
                    .setContentText(contentBody)
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.evening_icon)
                    //.setLargeIcon()
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifId, notification);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");

            prefsUtil.setNotificationMessage(contentBody);
            prefsUtil.setNotificationEmail(email);
            prefsUtil.setNotificationState(SharedPrefsUtil.ACTIVE_NOTIFICATION);
            prefsUtil.setNotificationTimeAlerted(sdf.format(new Date()));
        }
        else {

            Log.e(TAG, "ERROR: Push Notification received, but a user wasn't logged in");

        }

        setResultCode(Activity.RESULT_OK);
    }
}