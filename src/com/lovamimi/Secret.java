package com.lovamimi;

import android.util.Log;
import com.androidhive.jsonparsing.JSONParser;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Secret implements Serializable {
    private static final long serialVersionUID = 0x12345678L;
    private static final String TAG = "Secret";

    String sid;
    String body;
    String datetime;
    private String iconName;
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

    public String getCid() {
        // For comment.cid, we use numComments field
        return Integer.toString(this.numComments);
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
            Log.d("getSecrets", e.toString());
        }
        return null;
    }

    public boolean postLike(String sessionId) {
        return HttpHelper.postLovamimi(sessionId, new BasicNameValuePair("like_sid", sid));
    }

    public boolean postCommentLike(String sessionId) {
        Log.d("hage postCommentLike", sid + ":" + getCid());
        return HttpHelper.postLovamimi(sessionId,
                new BasicNameValuePair("like_sid", sid),
                new BasicNameValuePair("like_cid", getCid()));
    }

    public static boolean post(String sessionId, String text) {
        return HttpHelper.postLovamimi(sessionId, new BasicNameValuePair("text", text));
    }

    public static boolean postComment(String sessionId, String sid, String body) {
        return HttpHelper.postLovamimi(sessionId,
                new BasicNameValuePair("body", body),
                new BasicNameValuePair("sid", sid));
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
