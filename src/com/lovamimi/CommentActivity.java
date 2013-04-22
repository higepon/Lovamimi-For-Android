package com.lovamimi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		RelativeLayout incLayout = (RelativeLayout) mainLayout.findViewById(R.id.secret);
		TextView tv = (TextView) incLayout
				.findViewById(R.id.secret_body);
		tv.setText(secret.body);
		TextView secretDatetime = (TextView) incLayout
				.findViewById(R.id.secret_datetime);
		secretDatetime.setText(secret.datetime);

		
		TextView numComments = (TextView) incLayout
				.findViewById(R.id.num_comments);

		numComments.setText("コメント("
				+ String.valueOf(secret.numComments) + ")");
		ImageView commentIcon = (ImageView) incLayout
				.findViewById(R.id.comment_icon);
		
		commentIcon.setImageResource(secret.numComments > 0 ? R.drawable.comment : R.drawable.comment2);

		TextView numLikes = (TextView) incLayout
				.findViewById(R.id.num_likes);
		numLikes.setText("いいね(" + String.valueOf(secret.numLikes) + ")");
		
		ImageView icon = (ImageView) incLayout
				.findViewById(R.id.profile_image);
		icon.setImageResource(secret.getIconResource());		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

}
