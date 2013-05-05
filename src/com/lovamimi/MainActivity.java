package com.lovamimi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.facebook.Session;
import com.facebook.SessionState;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;

public class MainActivity extends BaseActivity {
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addSecretsToLayout(List<Secret> secrets) {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.layout_main);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for (Secret secret : secrets) {
            addOneSecretToLayout(mainLayout, inflater, secret);
        }
    }

    private void addOneSecretToLayout(LinearLayout mainLayout, LayoutInflater inflater, final Secret secret) {
        RelativeLayout incLayout = (RelativeLayout) inflater.inflate(R.layout.secret, null);
        TextView tv = (TextView) incLayout.findViewById(R.id.secret_body);
        tv.setText(secret.body);
        TextView secretDatetime = (TextView) incLayout.findViewById(R.id.secret_datetime);
        secretDatetime.setText(secret.datetime);

        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                intent.putExtra("secret", secret);
                startActivity(intent);
            }
        };

        TextView numComments = (TextView) incLayout.findViewById(R.id.num_comments);
        numComments.setOnClickListener(listener);
        numComments.setText("コメント(" + String.valueOf(secret.numComments) + ")");
        ImageView commentIcon = (ImageView) incLayout.findViewById(R.id.comment_icon);

        commentIcon.setImageResource(secret.numComments > 0 ? R.drawable.comment : R.drawable.comment2);
        commentIcon.setOnClickListener(listener);
        TextView numLikes = (TextView) incLayout.findViewById(R.id.num_likes);
        numLikes.setText("いいね(" + String.valueOf(secret.numLikes) + ")");
        OnClickListener likeListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("hahaha", "hahah");
            }
        };
        numLikes.setOnClickListener(likeListener);

        ImageView icon = (ImageView) incLayout.findViewById(R.id.profile_image);
        icon.setImageResource(secret.getIconResource());
        mainLayout.addView(incLayout);
    }

    private void getSecrets() {
        AsyncTask<String, Void, List<Secret>> fetchTimeline = new AsyncTask<String, Void, List<Secret>>() {

            @Override
            protected void onPostExecute(List<Secret> secrets) {
                super.onPostExecute(secrets);
                addSecretsToLayout(secrets);
                PullToRefreshScrollView pullToRefreshView = (PullToRefreshScrollView) findViewById(R.id.scroll);
                pullToRefreshView.onRefreshComplete();
                setProgressBarIndeterminateVisibility(Boolean.FALSE);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Secret> doInBackground(String... args) {
                return Secret.getSecrets();
            }
        };
        fetchTimeline.execute("test");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        PullToRefreshScrollView pullToRefreshView = (PullToRefreshScrollView) findViewById(R.id.scroll);
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getSecrets();
            }
        });
        track("Normal Secrets Loaded");
        getSecrets();
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void lovamimiLogin(String fbSessionId) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String sessionId) {
                super.onPostExecute(sessionId);
                setSessionId(sessionId);
                Intent intent = new Intent(MainActivity.this, PostSecretActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... strings) {
                String fbSessionId = strings[0];
                return com.lovamimi.Session.login(fbSessionId);
            }
        }.execute(fbSessionId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_status_update) {
            tryLogin();
            return true;
        }
        return false;
    }

    private void tryLogin() {
        // Facebook login
        Session.openActiveSession(this, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                lovamimiLogin(session.getAccessToken());
            }
        });
    }
}
