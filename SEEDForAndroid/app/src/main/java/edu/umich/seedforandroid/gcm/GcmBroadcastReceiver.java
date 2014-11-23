package edu.umich.seedforandroid.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GcmBroadcastReceiver extends BroadcastReceiver  {

    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)  {

        Bundle extras = intent.getExtras();
        Log.i(TAG, "Received message: " + extras.toString());
        Toast.makeText(context.getApplicationContext(), extras.get("message").toString(), Toast.LENGTH_SHORT).show();

        setResultCode(Activity.RESULT_OK);
    }
}