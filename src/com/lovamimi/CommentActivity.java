package com.lovamimi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CommentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Intent intent = getIntent();
		Secret secret = (Secret) intent.getExtras().get("secret");
		Log.d("debug", secret.toString());
		ScrollView mainLayout = (ScrollView) findViewById(R.id.scroll);
		RelativeLayout incLayout = (RelativeLayout) mainLayout.findViewById(R.id.secret_body_container);
		TextView tv = (TextView) incLayout.findViewById(R.id.secret_body);
		tv.setText(secret.body);
		TextView secretDatetime = (TextView) incLayout.findViewById(R.id.secret_datetime);
		secretDatetime.setText(secret.datetime);

		ImageView icon = (ImageView) incLayout.findViewById(R.id.profile_image);
		icon.setImageResource(secret.getIconResource());
		
		ImageView commentIcon = (ImageView)findViewById(R.id.comment_header_container).findViewById(R.id.profile_image);
		if (secret.comments.size() > 0) { 
			commentIcon.setImageResource(secret.comments.get(0).getIconResource());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

}
