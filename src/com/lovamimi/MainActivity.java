package com.lovamimi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.androidhive.jsonparsing.JSONParser;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MainActivity extends Activity {

	private ProgressDialog progressDialog;
	private MixpanelAPI mixpanel;

	@Override
	protected void onStart() {
		super.onStart();
		track("Normal Secrets Loaded");
		getSecrets();
	}

	private void track(String eventName) {
		mixpanel.track("Android:" + eventName, null);
	}

	private void getSecrets() {
		AsyncTask<String, Void, List<Secret>> fetchTimeline = new AsyncTask<String, Void, List<Secret>>() {

			@Override
			protected void onPostExecute(List<Secret> secrets) {
				super.onPostExecute(secrets);
				EditText timelintText = (EditText) findViewById(R.id.timeline_text);
				timelintText.setText("");
				for (Secret secret : secrets) {
					timelintText.setText(timelintText.getText().toString()
							+ "\n" + secret.body);
				}
				progressDialog.dismiss();				
			}

			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(MainActivity.this,
						"Refresh", "Please wait...");
				super.onPreExecute();
			}

			@Override
			protected List<Secret> doInBackground(String... args) {
				JSONParser parser = new JSONParser();
				JSONObject obj = parser
						.getJSONFromUrl("http://lovamimi.com/ja?json=1");
				Log.d(TAG, "obj=" + obj);
				ArrayList<Secret> results = new ArrayList<Secret>();
				try {
					JSONArray secrets = obj.getJSONArray("secrets");
					for (int i = 0; i < secrets.length(); i++) {
						JSONObject secret = secrets.getJSONObject(i);
						Log.d("", "secret=" + secret.getString("body"));
						results.add(new Secret(secret.getString("body")));
					}
					return results;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		fetchTimeline.execute("test");
	}

	private static final String TAG = "Lovamimi Client";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mixpanel = MixpanelAPI.getInstance(this, "e516b8643dc2d4d9b1779d243b7db7e5");
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			getSecrets();
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mixpanel.flush();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
