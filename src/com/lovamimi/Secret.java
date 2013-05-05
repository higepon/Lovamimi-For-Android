package com.lovamimi;

import android.util.Log;
import com.androidhive.jsonparsing.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Secret implements Serializable {
    private static final long serialVersionUID = 0x12345678L;
    private static final String TAG = "Secret";

    String sid;
    String body;
    String datetime;
    String iconName;
    int numComments;
    int numLikes;
    List<Secret> comments;

    public Secret(String sid, String body, String datetime, String iconName, int numComments, int numLikes) {
        this.sid = sid;
        this.body = body;
        this.datetime = datetime;
        this.iconName = iconName;
        this.numComments = numComments;
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Secret{" +
                "sid='" + sid + '\'' +
                ", body='" + body + '\'' +
                ", datetime='" + datetime + '\'' +
                ", iconName='" + iconName + '\'' +
                ", numComments=" + numComments +
                ", numLikes=" + numLikes +
                ", comments=" + comments +
                '}';
    }

    public int getIconResource() {
        if (iconName.equals("0.jpg")) {
            return R.drawable.lovamimi_0;
        } else if (iconName.equals("1.jpg")) {
            return R.drawable.lovamimi_1;
        } else if (iconName.equals("2.jpg")) {
            return R.drawable.lovamimi_2;
        } else if (iconName.equals("2.jpg")) {
            return R.drawable.lovamimi_2;
        } else if (iconName.equals("3.jpg")) {
            return R.drawable.lovamimi_3;
        } else if (iconName.equals("4.jpg")) {
            return R.drawable.lovamimi_4;
        } else if (iconName.equals("5.jpg")) {
            return R.drawable.lovamimi_5;
        } else if (iconName.equals("6.jpg")) {
            return R.drawable.lovamimi_6;
        } else if (iconName.equals("7.jpg")) {
            return R.drawable.lovamimi_7;
        } else if (iconName.equals("8.jpg")) {
            return R.drawable.lovamimi_8;
        } else if (iconName.equals("9.jpg")) {
            return R.drawable.lovamimi_9;
        } else {
            return R.drawable.lovamimi_0;
        }
    }

    public static List<Secret> getSecrets() {
        JSONParser parser = new JSONParser();
        JSONObject obj = parser.getJSONFromUrl("http://lovamimi.com/ja?json=1");
        ArrayList<Secret> results = new ArrayList<Secret>();
        try {
            JSONArray secrets = obj.getJSONArray("secrets");
            Secret.extractSecrets(results, secrets);
            return results;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public boolean postLike(String sessionId) {
        sessionId = HttpHelper.chop(sessionId);
        HttpClient client = new DefaultHttpClient();
        String url = "http://lovamimi.com/ja";
        HttpPost httpPost = new HttpPost(url);
        BasicHttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("SESSION_ID", sessionId);
        cookie.setVersion(1);
        cookie.setDomain("lovamimi.com");
        cookie.setPath("/");
        cookie.setExpiryDate(new Date(2032, 5, 16));
        cookieStore.addCookie(cookie);
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("like_sid", sid));
        params.add(new BasicNameValuePair("lang", "ja"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = client.execute(httpPost, httpContext);
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                String result = HttpHelper.streamToString(in);
                in.close();
                Log.e(TAG, result);
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    // todo: Extract http post
    public static boolean post(String sessionId, String text) {
        if (Config.isFakeWrite) {
            return false;
        }
        // Server side bug, sessionId has "\n" on it's tail.
        sessionId = HttpHelper.chop(sessionId);
        HttpClient client = new DefaultHttpClient();
        String url = "http://lovamimi.com/ja";
        HttpPost httpPost = new HttpPost(url);
        BasicHttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("SESSION_ID", sessionId);
        cookie.setVersion(1);
        cookie.setDomain("lovamimi.com");
        cookie.setPath("/");
        cookie.setExpiryDate(new Date(2032, 5, 16));
        cookieStore.addCookie(cookie);
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("text", text));
        params.add(new BasicNameValuePair("lang", "ja"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = client.execute(httpPost, httpContext);
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                String result = HttpHelper.streamToString(in);
                in.close();
                Log.e(TAG, result);
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    private static void extractSecrets(ArrayList<Secret> results, JSONArray secrets) throws JSONException {
        for (int i = 0; i < secrets.length(); i++) {
            JSONObject secret = secrets.getJSONObject(i);
            JSONArray commentsArray = secret.has("comments") ? secret.getJSONArray("comments") : null;
            ArrayList<Secret> comments;
            if (commentsArray == null) {
                comments = new ArrayList<Secret>(0);
            } else {
                comments = new ArrayList<Secret>(commentsArray.length());
                extractSecrets(comments, commentsArray);
            }
            Log.d("", "secret=" + secret.getString("body"));
            Secret s = new Secret(secret.getString("sid"), secret.getString("body"), secret.getString("datetime"), secret.getString("icon"),
                    secret.getInt("num_comments"), secret.getInt("num_likes"));
            s.comments = comments;
            results.add(s);
        }
    }
}
