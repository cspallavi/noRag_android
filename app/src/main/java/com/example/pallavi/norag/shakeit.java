package com.example.pallavi.norag;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class shakeit extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        boolean serviceRunning = isMyServiceRunning(SensorService.class, context);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shakeit);

            Intent newIntent = new Intent(context, SensorService.class);
            newIntent.setAction("START");
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, newIntent, 0);

            Intent newIntent2 = new Intent(context, SensorService.class);
            newIntent2.setAction("STOP");
            PendingIntent pendingIntent2 = PendingIntent.getService(context, 0, newIntent2, 0);

            views.setOnClickPendingIntent(R.id.notshake, pendingIntent2);
            views.setOnClickPendingIntent(R.id.shake,pendingIntent);
            if(serviceRunning) {
                views.setViewVisibility(R.id.shake, View.GONE);
                views.setViewVisibility(R.id.notshake, View.VISIBLE);
            }
            else{
                views.setViewVisibility(R.id.shake, View.VISIBLE);
                views.setViewVisibility(R.id.notshake, View.GONE);
            }
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private boolean isMyServiceRunning(Class<?> serviceClass,  Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

