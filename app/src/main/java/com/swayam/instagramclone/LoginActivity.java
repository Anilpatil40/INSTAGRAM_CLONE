package com.swayam.instagramclone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    private EditText userField,passField;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        userField = findViewById(R.id.username);
        passField = findViewById(R.id.username);
    }

    public void login(View view){
        String username = userField.getText().toString();
        String password = passField.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e){
                if(e == null){
                    Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                    intent.putExtra("USER", user.getUsername());
                    startActivity(intent);
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
                        userField.setText("");
                        passField.setText("");
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
    }

}