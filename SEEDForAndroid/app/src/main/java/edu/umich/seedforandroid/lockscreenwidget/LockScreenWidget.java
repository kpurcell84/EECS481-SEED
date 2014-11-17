package edu.umich.seedforandroid.lockscreenwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class LockScreenWidget extends AppWidgetProvider  {

    @Override
    public void onEnabled(Context context)  {

        super.onEnabled(context);

        // Lock Screen Widget Servicer + Receiver
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LockScreenWidgetReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, 1000, pi); //1000
    }

    @Override
    public void onDisabled(Context context)  {

        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)  {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }
}
