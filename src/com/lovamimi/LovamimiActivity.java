package com.lovamimi;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.app.Activity;
import android.os.Bundle;

public class LovamimiActivity extends Activity {

	protected MixpanelAPI mixpanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mixpanel = MixpanelAPI.getInstance(this, "e516b8643dc2d4d9b1779d243b7db7e5");
	}

	protected void track(String eventName) {
		mixpanel.track("Android:" + eventName, null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mixpanel.flush();
	}
}
