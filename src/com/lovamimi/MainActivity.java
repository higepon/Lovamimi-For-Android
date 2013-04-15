package com.lovamimi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidhive.jsonparsing.JSONParser;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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
				return null;
			}
		}.execute("test");		
	}

	private static final String TAG = "Lovamimi Client";
	private static final int DEFAULT_TIMEOUT = 10000;
	private static final String DEFAULT_USER_AGENT = "Lovamimi For Android/1.0";

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
