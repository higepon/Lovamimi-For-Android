package com.lovamimi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class BaseActivity extends Activity {

    protected MixpanelAPI mixpanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mixpanel = MixpanelAPI.getInstance(this, "e516b8643dc2d4d9b1779d243b7db7e5");
    }

    protected void track(String eventName) {
        mixpanel.track("Android:" + eventName, null);
    }

    protected void error(String s) {
        Log.e(this.getClass().toString(), s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mixpanel.flush();
    }

    public String getSessionId() {
        LovamimiApplication app = (LovamimiApplication) getApplication();
        return app.getSessionId();
    }

    public void setSessionId(String sessionId) {
        LovamimiApplication app = (LovamimiApplication) getApplication();
        app.setSessionId(sessionId);
    }
}
