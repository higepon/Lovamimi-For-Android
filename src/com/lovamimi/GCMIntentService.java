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
        // TODO Auto-generated method stub
        Log.i(TAG, "onError : " + arg1 );
    }

    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
/*
        String str = arg1.getStringExtra("message");
        NotificationManager notim = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_launcher, str, System.currentTimeMillis());
        Intent inte = new Intent();
        PendingIntent cont = PendingIntent.getActivity(this, 0, inte, 0);
        notif.setLatestEventInfo(getApplicationContext(), str, TAG, cont);
        notim.notify(R.string.app_name, notif);
*/
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.lovamimi_logo)
                        .setContentTitle("ロバ耳")
                        .setContentText(arg1.getStringExtra("message"));
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(R.string.app_id, mBuilder.build());
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