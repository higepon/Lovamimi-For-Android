package com.lovamimi;

import android.app.Application;

public class LovamimiApplication extends Application {
    private String sessionId = null;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
