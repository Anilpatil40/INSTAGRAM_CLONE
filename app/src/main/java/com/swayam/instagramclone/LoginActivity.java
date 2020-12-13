package com.swayam.instagramclone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity{
    private EditText userField,passField;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        userField = findViewById(R.id.username);
        passField = findViewById(R.id.password);
    }

    public void login(View view){
        String username = userField.getText().toString();
        String password = passField.getText().toString();

        if(username.equals("") || password.equals(""))
            showMessage("Username and password can not be empty");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("loading...");
        dialog.show();

        ParseUser.logInInBackground(username, password, new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e){
                dialog.dismiss();
                if(e == null){
                    Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    showMessage(e.getMessage());
                }
            }
        });

    }

    private void showMessage(String message){
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("ERROR")
                .setMessage(message)
                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        System.exit(0);
                    }
                })
                .create()
                .show();
    }

    public void createUser(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

}