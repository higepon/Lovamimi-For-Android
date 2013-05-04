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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LovamimiActivity {

    private String sessionId = null;

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

    private void login(String sessionToken) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
                Log.d("hoge", "session=" + s);
                synchronized(sessionId) {
                    sessionId = s;
                }
            }

            public String convertStreamToString(InputStream is) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return sb.toString();
            }

            @Override
            protected String doInBackground(String... strings) {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://lovamimi.com/ja/login.scm");

                List<NameValuePair> postlist = new ArrayList<NameValuePair>();
                postlist.add(new BasicNameValuePair("type", "ios"));
                postlist.add(new BasicNameValuePair("lang", "ja"));
                postlist.add(new BasicNameValuePair("token", strings[0]));

                try {
                    post.setEntity(new UrlEncodedFormEntity(postlist));
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError("Encoding Error");
                }

                try {
                    HttpResponse response = client.execute(post);
                    Log.d("hige", response.getStatusLine().toString());

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
// A Simple JSON Response Read
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);


                        instream.close();
                        return result;
                    }
                    return "";
                } catch (IOException e) {
                    throw new AssertionError("IO Error");
                }
            }
        }.execute(sessionToken);

/*
    HERE();
    NSURL* url = [NSURL URLWithString:@"http://lovamimi.com/ja/login.scm"];

    NSUserDefaults* ud = [NSUserDefaults standardUserDefaults];
    NSString* device_token = [ud stringForKey:@"DEVICE_TOKEN"];

    NSString* extraParam = device_token == nil ? @"" : [NSString stringWithFormat:@"&device_token=%@", device_token];

    NSData *myRequestData = [[NSString stringWithFormat:@"type=ios&lang=ja&token=%@%@", session.accessToken, extraParam] dataUsingEncoding:NSUTF8StringEncoding];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL: url];
    [request setHTTPMethod: @"POST"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"content-type"];
    [request setHTTPBody: myRequestData];

    NSURLConnection *conn = [NSURLConnection connectionWithRequest:request delegate:self];
    if (conn == nil) {
        [self.delegate onLoginError:@"接続エラー"];
        return;
    }
         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_status_update) {
            // start Facebook Login
            Session.openActiveSession(this, true, new Session.StatusCallback() {

                // callback when session changes state
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    Log.d("hoge", "facebook callback!" + session.getAccessToken());
                    login(session.getAccessToken());
                }
            });
            return true;
        }
        return false;
    }
}
