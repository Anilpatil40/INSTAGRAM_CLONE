package com.swayam.instagramclone;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity{
    private EditText emailEditText,usernameEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void registerEmail(View view){
        String email = emailEditText.getText().toString();
        String user = usernameEditText.getText().toString();
        String pass = passwordEditText.getText().toString();

        if(email.equals("") || user.equals("") || pass.equals("")){
            showUnSuccessfulMessage("Fields can not be empty");
            return;
        }

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user);
        parseUser.setEmail(email);
        parseUser.setPassword(pass);

        parseUser.signUpInBackground(new SignUpCallback(){
            @Override
            public void done(ParseException e){
                if(e == null){
                    ParseUser.logOut();
                    showSuccessMessage();
                }else{
                    showUnSuccessfulMessage(e.getMessage());
                }
            }
        });
    }

    private void showSuccessMessage(){
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("Successful")
                .setMessage("User created successfully please verify email before login")
                .setPositiveButton("GO BACK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        onBackPressed();
                    }
                })
                .create()
                .show();
    }

    private void showUnSuccessfulMessage(String message){
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("GO BACK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        RegisterActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("RETRY", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        emailEditText.setText("");
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    }
                })
                .create()
                .show();
    }

    public void openLogin(View view){
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
}