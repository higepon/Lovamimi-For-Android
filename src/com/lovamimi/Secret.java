package com.lovamimi;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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

import android.util.Log;

import com.androidhive.jsonparsing.JSONParser;

public class Secret implements Serializable {
	private static final long serialVersionUID = 0x12345678L;

	String body;
	String datetime;
	String iconName;
	int numComments;
	int numLikes;
	List<Secret> comments;

	@Override
	public String toString() {
		return "Secret [body=" + body + ", datetime=" + datetime + ", iconName=" + iconName + ", numComments="
				+ numComments + ", numLikes=" + numLikes + "]";
	}

	public Secret(String body, String datetime, String iconName, int numComments, int numLikes) {
		super();
		this.body = body;
		this.datetime = datetime;
		this.iconName = iconName;
		this.numComments = numComments;
		this.numLikes = numLikes;
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

    // todo move
    public static String chop(String s) {
        if (s.isEmpty()) {
            return s;
        } else {
            if (s.charAt(s.length() - 1) == '\n') {
                return s.substring(0, s.length() - 1);
            } else {
                return s;
            }
        }
    }

    public static boolean post(String sessionId, String text) {
        // Server side bug, sessionId has "\n" on it's tail.
        sessionId = Secret.chop(sessionId);
        HttpClient client = new DefaultHttpClient();
        String url = "http://lovamimi.com/ja";
        HttpPost httpPost = new HttpPost(url);
        BasicHttpContext mHttpContext = new BasicHttpContext();
        CookieStore mCookieStore      = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("SESSION_ID", sessionId);
        cookie.setVersion(1);
        cookie.setDomain("lovamimi.com");
        cookie.setPath("/");
        cookie.setExpiryDate(new Date(2014, 5, 16));
        Log.d("hage", cookie.toString());
        mCookieStore.addCookie(cookie);
        mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
        Log.d("hage", "last<" + sessionId.charAt(sessionId.length() - 1) + ">");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", text));
        params.add(new BasicNameValuePair("lang", "ja"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            Log.d("POST", mHttpContext.toString());
            HttpResponse response = client.execute(httpPost, mHttpContext);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            String result = convertStreamToString(in);
            in.close();
            Log.d("HAGe", result);
            return true;
        } catch (IOException e) {
            throw new AssertionError("IO Error");
        }
    }

    // duplicate
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
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
/*
    NSMutableURLRequest* request = [Secret createLovamimiRequest:@"http://lovamimi.com/ja" requestBody:requestBody];
    SecretPoster* poster = [[SecretPoster alloc] initWithDelegate:delegate];
    NSURLConnection* conn = [[NSURLConnection alloc] initWithRequest:request delegate:poster];
    if (conn == nil) {
        [delegate onSecretPostComplete:@"接続エラー"];
        return;
    }
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    if (requestBody != nil) {
        request.HTTPMethod = @"POST";
        request.HTTPBody = [requestBody dataUsingEncoding:NSUTF8StringEncoding];
    } else {
        request.HTTPMethod = @"GET";
    }
    AppDelegate* appDelegate = (AppDelegate*)[[UIApplication sharedApplication] delegate];
    assert(appDelegate);
    NSDictionary *properties = [NSDictionary dictionaryWithObjectsAndKeys:
                              @"lovamimi.com", NSHTTPCookieDomain,
                              @"/", NSHTTPCookiePath,
                              @"SESSION_ID", NSHTTPCookieName,
                              appDelegate.session.id, NSHTTPCookieValue,
                              nil];
    NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:properties];
    NSArray* cookies = [NSArray arrayWithObjects: cookie, nil];
    NSDictionary * headers = [NSHTTPCookie requestHeaderFieldsWithCookies:cookies];
    [request setAllHTTPHeaderFields:headers];
    return request;
     */
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
			Secret s = new Secret(secret.getString("body"), secret.getString("datetime"), secret.getString("icon"),
					secret.getInt("num_comments"), secret.getInt("num_likes"));
			s.comments = comments;
			results.add(s);
		}
	}
}
