package com.lovamimi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
				addSecretsToLayout(secrets);
				progressDialog.dismiss();
			}

			private void addSecretsToLayout(List<Secret> secrets) {
				LinearLayout mainLayout = (LinearLayout) findViewById(R.id.layout_main);
				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				for (Secret secret : secrets) {
					addOneSecretToLayout(mainLayout, inflater, secret);
				}
			}

			private void addOneSecretToLayout(LinearLayout mainLayout,
					LayoutInflater inflater, Secret secret) {
				RelativeLayout incLayout = (RelativeLayout) inflater.inflate(
						R.layout.secret, null);
				TextView tv = (TextView) incLayout
						.findViewById(R.id.secret_body);
				tv.setText(secret.body);
				TextView secretDatetime = (TextView) incLayout
						.findViewById(R.id.secret_datetime);
				secretDatetime.setText(secret.datetime);

				OnClickListener listener = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.d("tap", "tap");
					}
				};
				
				TextView numComments = (TextView) incLayout
						.findViewById(R.id.num_comments);
				numComments.setOnClickListener(listener);
				numComments.setText("コメント("
						+ String.valueOf(secret.numComments) + ")");
				ImageView commentIcon = (ImageView) incLayout
						.findViewById(R.id.comment_icon);
				
				commentIcon.setImageResource(secret.numComments > 0 ? R.drawable.comment : R.drawable.comment2);
				commentIcon.setOnClickListener(listener);
				TextView numLikes = (TextView) incLayout
						.findViewById(R.id.num_likes);
				numLikes.setText("いいね(" + String.valueOf(secret.numLikes) + ")");
				
				ImageView icon = (ImageView) incLayout
						.findViewById(R.id.profile_image);
				icon.setImageResource(secret.getIconResource());
				mainLayout.addView(incLayout);
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
						results.add(new Secret(
								secret.getString("body"),
								secret.getString("datetime"),
								secret.getString("icon"),
								secret.getInt("num_comments"),
								secret.getInt("num_likes")));
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
		mixpanel = MixpanelAPI.getInstance(this,
				"e516b8643dc2d4d9b1779d243b7db7e5");
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
