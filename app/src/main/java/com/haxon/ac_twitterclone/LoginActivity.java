package com.haxon.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginActivity, btnSignUpLoginActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginActivity = findViewById(R.id.btnLoginActivity);
        btnSignUpLoginActivity = findViewById(R.id.btnSignUpLoginActivity);

        btnLoginActivity.setOnClickListener(LoginActivity.this);
        btnSignUpLoginActivity.setOnClickListener(LoginActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginActivity:
                if (edtLoginPassword.getText().toString().equals("") || edtLoginEmail.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Enter correct Email or Password",Toast.LENGTH_LONG).show();
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging in User");
                    progressDialog.show();
                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null){
                                Toast.makeText(LoginActivity.this,"Log in Successful",Toast.LENGTH_LONG).show();
                                transitionToTwitterUsers();
                            }else{
                                Toast.makeText(LoginActivity.this,e.getMessage()+"\nTry Again",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnSignUpLoginActivity:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void transitionToTwitterUsers(){
        Intent intent = new Intent(LoginActivity.this, TwitterUsers.class);
        startActivity(intent);
    }
}
