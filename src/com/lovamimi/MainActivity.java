package com.lovamimi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
//import com.facebook.Session;
import com.google.android.gcm.GCMRegistrar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;

public class MainActivity extends BaseActivity {
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.facebook.Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addSecretsToLayout(List<Secret> secrets) {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.layout_main);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mainLayout.removeAllViews();
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

        commentIcon.setImageResource(secret.numComments > 0 ? R.drawable.comment2 : R.drawable.comment);
        commentIcon.setOnClickListener(listener);
        TextView numLikes = (TextView) incLayout.findViewById(R.id.num_likes);
        numLikes.setText("いいね(" + String.valueOf(secret.numLikes) + ")");
        OnClickListener likeListener = new OnClickListener() {

            @Override
            public void onClick(final View v) {
                track("Liked secret");
                new AsyncTask<Secret, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        TextView view = (TextView) v;
                        secret.numLikes++;
                        view.setText("いいね(" + String.valueOf(secret.numLikes) + ")");
                    }

                    @Override
                    protected Boolean doInBackground(Secret... secrets) {
                        if (getSessionId() == null) {
                            return false;
                        }
                        Secret secret = secrets[0];
                        return secret.postLike(getSessionId());
                    }

                    @Override
                    protected void onPostExecute(Boolean isLikePosted) {
                        super.onPostExecute(isLikePosted);
                    }
                }.execute(secret);

            }
        };
        numLikes.setOnClickListener(likeListener);

        ImageView icon = (ImageView) incLayout.findViewById(R.id.profile_image);
        icon.setImageResource(secret.getIconResource());
        mainLayout.addView(incLayout);
    }

    private void syncLoginStatus() {
        // todo move to session class?
        AsyncTask<Void, Void, Void> fetchTimeline = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void notUsed) {
                super.onPostExecute(notUsed);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void ... args) {
                LovamimiApplication app = (LovamimiApplication) getApplication();
                app.syncSession();
                return null;
            }
        };
        fetchTimeline.execute();
    }


    private void getSecrets() {
        AsyncTask<Void, Void, List<Secret>> fetchTimeline = new AsyncTask<Void, Void, List<Secret>>() {

            @Override
            protected void onPostExecute(List<Secret> secrets) {
                super.onPostExecute(secrets);
                SecretsCache secretsCache = new SecretsCache(MainActivity.this);
                for (Secret secret : secrets) {
                    secretsCache.insertSecret(secret);
                }
                secretsCache.debug();
                Log.d("MOGO", secretsCache.getSecret(secrets.get(0).sid).body);

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
            protected List<Secret> doInBackground(Void ... args) {
                return Secret.getSecrets();
            }
        };
        fetchTimeline.execute();
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
                syncLoginStatus();
            }
        });
        track("Normal Secrets Loaded");

        try {
            GCMRegistrar.checkDevice(getApplicationContext());
            GCMRegistrar.checkManifest(getApplicationContext());

            String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
            if (TextUtils.isEmpty(regId)) {
                GCMRegistrar.register(getApplicationContext(), "372350520876");
            } else {
                LovamimiApplication app = (LovamimiApplication) getApplication();
                app.setDeviceToken(regId);
                i("already registered");
            }
        } catch (Exception e) {
            e("GCM error: " + e.toString());
        }
        getSecrets();
        syncLoginStatus();
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_status_update) {
            loginAndNextActivity(PostSecretActivity.class);
            return true;
        }
        return false;
    }
}
