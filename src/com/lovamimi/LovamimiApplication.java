package com.lovamimi;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import com.deploygate.sdk.DeployGate;

public class LovamimiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this, null, true);
        if (Config.forceLogin) {
            clearSession();
        }
    }

    private void clearSession() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        sessionId = null;
    }

    private String sessionId = null;
    private static final String PREFS_NAME = "lovamiomi_settings";

    public String getSessionId() {
        if (sessionId != null) {
            return sessionId;
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("sessionId", null);
    }

    public void setSessionId(String sessionId) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sessionId", sessionId);
        editor.commit();
        this.sessionId = sessionId;
    }

    public String getDeviceToken() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("deviceToken", null);
    }

    public void setDeviceToken(String deviceToken) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("deviceToken", deviceToken);
        editor.commit();
    }

    public void syncSession() {
        sessionId = getSessionId();
        Log.i("Session", "Start sync session" + getSessionId());
        if (sessionId != null && Session.isExpired(sessionId)) {
            Log.i("Session", "session expired");
            clearSession();
        }
        Log.i("Session", "End sync session");
    }
}
