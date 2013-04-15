package com.lovamimi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.androidhive.jsonparsing.JSONParser;

public class MainActivity extends Activity {

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		AsyncTask<String, Void, String> postToTwitterTask = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... args) {
				JSONParser parser = new JSONParser();
				JSONObject obj = parser.getJSONFromUrl("http://lovamimi.com/ja?json=1");
				Log.d(TAG, "obj=" + obj);
				try {
					JSONArray secrets = obj.getJSONArray("secrets");
					 for(int i = 0; i < secrets.length(); i++){
						 JSONObject secret = secrets.getJSONObject(i);					
						 Log.d("", "secret=" + secret.getString("body"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute("test");		
	}

	private static final String TAG = "Lovamimi Client";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
