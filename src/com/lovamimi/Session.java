package com.lovamimi;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Session {

    public static String login(String fbSessionId, String deviceToken) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://lovamimi.com/ja/login.scm");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", "ios"));
        params.add(new BasicNameValuePair("lang", "ja"));
        params.add(new BasicNameValuePair("token", fbSessionId));
        if (deviceToken != null) {
            params.add(new BasicNameValuePair("android_device_token", deviceToken));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Encoding Error");
        }

        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream in = entity.getContent();
                String result = HttpHelper.streamToString(in);
                in.close();
                return result;
            }
            return "";
        } catch (IOException e) {
            throw new AssertionError("IO Error");
        }
    }
}
