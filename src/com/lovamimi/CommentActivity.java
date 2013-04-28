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

// scribe
// layout cleanup
public class CommentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Secret secret = (Secret) getIntent().getExtras().get("secret");
		assert(secret != null);
		setSecretToLayout(secret);
		
		ImageView commentIcon = (ImageView)findViewById(R.id.comment_container).findViewById(R.id.profile_image);
		TextView dateText = (TextView) findViewById(R.id.comment_container).findViewById(R.id.secret_datetime);
		TextView commentBody = (TextView) findViewById(R.id.comment_body);
		TextView numLikes = (TextView)findViewById(R.id.comment_container).findViewById(R.id.num_likes);		
		if (secret.comments.size() > 0) { 
			Secret comment = secret.comments.get(0);
			commentIcon.setImageResource(comment.getIconResource());
			dateText.setText(comment.datetime);
			commentBody.setText(comment.body);
			numLikes.setText("いいね(" + String.valueOf(secret.numLikes) + ")");			
		}
	}

	private void setSecretToLayout(Secret secret) {
		TextView tv = (TextView)findViewById(R.id.secret_body);
		tv.setText(secret.body);
		TextView secretDatetime = (TextView)findViewById(R.id.secret_datetime);
		secretDatetime.setText(secret.datetime);
		ImageView icon = (ImageView)findViewById(R.id.profile_image);
		icon.setImageResource(secret.getIconResource());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

}
