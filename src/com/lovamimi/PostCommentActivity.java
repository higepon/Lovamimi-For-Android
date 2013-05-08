package com.lovamimi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostCommentActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_secret);
        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText secretText = (EditText) findViewById(R.id.secret_text);
                String sid = getIntent().getExtras().getString("sid");
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected void onPostExecute(Boolean isPosted) {
                        super.onPostExecute(isPosted);
                        if (isPosted) {
                            Intent intent = new Intent(PostCommentActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PostCommentActivity.this, "投稿に失敗しました。時間をおいてもう一度お試し下さい。", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return Secret.postComment(strings[0], strings[1], strings[2]);
                    }
                }.execute(getSessionId(), sid, secretText.getText().toString());
            }
        });
    }
}