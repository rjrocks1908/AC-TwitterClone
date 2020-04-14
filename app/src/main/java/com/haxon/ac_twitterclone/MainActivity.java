package com.haxon.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtSignUpEmail, edtSignUpUsername, edtSignUpPassword;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(MainActivity.this);
        btnSignUp.setOnClickListener(MainActivity.this);

        if (ParseUser.getCurrentUser()!=null){
            //ParseUser.logOut();
            transitionToTwitterUsers();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                if (edtSignUpEmail.getText().toString().equals("") || edtSignUpPassword.getText().toString().equals("") ||
                        edtSignUpUsername.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Please Enter Email, Username and Password",Toast.LENGTH_LONG).show();
                }else {
                    ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtSignUpEmail.getText().toString());
                    appUser.setUsername(edtSignUpUsername.getText().toString());
                    appUser.setPassword(edtSignUpPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up " + edtSignUpUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(MainActivity.this,"Sign Up Successful",Toast.LENGTH_LONG).show();
                                transitionToTwitterUsers();
                            }else{
                                Toast.makeText(MainActivity.this,e.getMessage() + "\nTry Again",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLogin:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void transitionToTwitterUsers (){
        Intent intent = new Intent(MainActivity.this, TwitterUsers.class);
        startActivity(intent);
    }
}
