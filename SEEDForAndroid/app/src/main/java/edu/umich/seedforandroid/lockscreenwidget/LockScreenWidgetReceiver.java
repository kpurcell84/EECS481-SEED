package edu.umich.seedforandroid.lockscreenwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockScreenWidgetReceiver extends BroadcastReceiver  {

    public LockScreenWidgetReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent)  {

        Intent i = new Intent(context, LockScreenWidgetServicer.class);
        context.getApplicationContext().startService(i);
    }
}