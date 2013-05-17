package com.lovamimi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";

    @Override
    protected void onError(Context arg0, String arg1) {
        Log.e(TAG, "onError : " + arg1 );
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.lovamimi_logo)
                .setContentTitle("ロバ耳")
                .setContentText(intent.getStringExtra("message"));

        Intent resultIntent = new Intent(this, MainActivity.class);

        // To make sure that the back button navigates to home screen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.app_id, builder.build());
    }

    @Override
    protected void onRegistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        LovamimiApplication app = (LovamimiApplication)getApplication();
        app.setDeviceToken(arg1);
        Log.i(TAG, "onRegistered : " + arg1 );
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onUnregistered : " + arg1 );
    }
}