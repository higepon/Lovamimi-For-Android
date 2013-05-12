package com.lovamimi;

import android.util.Log;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HttpHelper {
    private static final String TAG = "HttpHelper";

    private static String chop(String s) {
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

    public static boolean postLovamimi(String sessionId, BasicNameValuePair... queryParameters) {
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
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(queryParameters));
        params.add(new BasicNameValuePair("lang", "ja"));
        params.add(new BasicNameValuePair("type", "android"));
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

    public static String streamToString(InputStream is) {
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
}
