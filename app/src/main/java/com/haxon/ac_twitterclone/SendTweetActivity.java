package com.haxon.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtTweet;
    private Button btnSendTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        btnSendTweet = findViewById(R.id.btnSendTweet);

        btnSendTweet.setOnClickListener(SendTweetActivity.this);
    }

    @Override
    public void onClick(View v) {
        ParseObject parseObject = new ParseObject("MyTweet");
        parseObject.put("tweet", edtTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(SendTweetActivity.this,
                            ParseUser.getCurrentUser().getUsername()+"'s tweet("+edtTweet.getText().toString()+" is saved",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(SendTweetActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}
