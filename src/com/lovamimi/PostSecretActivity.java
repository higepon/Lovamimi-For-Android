package com.lovamimi;

import android.app.Activity;
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
                Secret.post(getSessionId(), secretText.getText().toString());
            }
        });
    }
}