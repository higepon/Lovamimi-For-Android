package com.lovamimi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PostSecretActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_secret);
        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText secretText = (EditText) findViewById(R.id.secret_text);
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                    }

                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return Secret.post(strings[0], strings[1]);
                    }
                }.execute(getSessionId(), secretText.getText().toString());
            }
        });
    }
}