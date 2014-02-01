package com.lovamimi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";

    @Override
    protected void onError(Context context, String s) {
        Log.e(TAG, "onError : " + s);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

        Intent resultIntent = new Intent(this, MainActivity.class);

        String sid = intent.getStringExtra("sid");
        Secret secret = null;
        if (sid != null) {
            secret = (new SecretsCache(context)).getSecret(sid);
        }

        String message = intent.getStringExtra("message");
        String title = "ロバ耳 - " + message;

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.lovamimi_logo)
                .setContentTitle(title)
                .setContentText(secret.body);

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
    protected void onRegistered(Context context, String s) {
        LovamimiApplication app = (LovamimiApplication) getApplication();
        app.setDeviceToken(s);
        Log.i(TAG, "onRegistered : " + s);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        Log.i(TAG, "onUnregistered : " + s);
    }
}