package com.swayam.instagramclone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class UserDetailsFragment extends Fragment {
    private EditText usernameFiled,nameFiled,emailField,aboutField;
    private ProgressBar progressBar;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameFiled = getView().findViewById(R.id.usernameEditText);
        nameFiled = getView().findViewById(R.id.nameEditText);
        emailField = getView().findViewById(R.id.emailEditText);
        aboutField = getView().findViewById(R.id.aboutEditText);

        progressBar = getView().findViewById(R.id.progressbar);
        scrollView = getView().findViewById(R.id.scrollview);

        Button button = getView().findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        showData();
    }

    private void showData(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    for(ParseUser parseUser : users) {
                        usernameFiled.setText(parseUser.getUsername());
                        nameFiled.setText(checkNull(parseUser.get("name")));
                        emailField.setText(parseUser.getEmail());
                        aboutField.setText(checkNull(parseUser.get("about")));

                        progressBar.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void submit(View view){

        String name = nameFiled.getText().toString();
        String about = aboutField.getText().toString();

        ParseUser parseUser = ParseUser.getCurrentUser();
        //parseUser.add("image", );
        parseUser.put("name",name);
        parseUser.put("about",about);

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Saving...");
        dialog.show();

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e == null){
                    showSuccessfulMessage();
                }else {
                    showUnSuccessfulMessage(e.getMessage());
                }
            }
        });
    }

    private void showSuccessfulMessage(){
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Successful")
                .setMessage("user updates successfully")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        showData();
                    }
                })
                .create()
                .show();
    }

    private void showUnSuccessfulMessage(String message){
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        showData();
                    }
                })
                .create()
                .show();
    }

    private String checkNull(Object object){
        if (object == null){
            return "";
        }
        return object.toString();
    }


}