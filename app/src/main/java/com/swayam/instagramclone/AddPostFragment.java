package com.swayam.instagramclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;

public class AddPostFragment extends Fragment {
    private TextView tagLine,tagLineButton,insertImage,removeImage;
    private Button submitButton;
    private ImageView imageView;
    private String imagePath = null;
    private ParseUser parseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_post,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parseUser = ParseUser.getCurrentUser();

        TextView username = getView().findViewById(R.id.username);
        username.setText(parseUser.getUsername());
        tagLine = getView().findViewById(R.id.tagLine);
        tagLine.setText("");
        tagLineButton = getView().findViewById(R.id.editTagLine);
        submitButton = getView().findViewById(R.id.postButton);
        imageView = getView().findViewById(R.id.imageView);
        insertImage = getView().findViewById(R.id.insert_image);
        removeImage = getView().findViewById(R.id.remove_image);

        tagLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagLine();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = null;
                imageView.setImageBitmap(null);
            }
        });
    }

    private void selectImage(){
        if (!checkReadExternalStoragePermission())
            return;
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }

    private boolean checkReadExternalStoragePermission(){
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if ( getContext().checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission},
                    2);
            return false;
        }
    }

    private void editTagLine(){

        final EditText editText = new EditText(getContext());
        editText.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        editText.setText(tagLine.getText().toString());

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Content Message")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagLine.setText(editText.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void uploadPost(){
        ParseUser parseUser = ParseUser.getCurrentUser();

        ParseObject object = new ParseObject("Posts");
        object.put("username",parseUser.getUsername());
        object.put("tagline",tagLine.getText().toString());
        if (imagePath != null){
            ParseFile file = new ParseFile(new File(imagePath));
            object.put("image",file);
        }

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("uploading...");
        dialog.show();

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if(e == null){
                    FancyToast.makeText(getContext(),"Uploaded",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                }else {
                    FancyToast.makeText(getContext(),e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }
                tagLine.setText("");
                imageView.setImageBitmap(null);
            }
        });
    }

    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            if (resultCode != getActivity().RESULT_OK && data == null)
                return;
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    FancyToast.makeText(getContext(),"permission denied",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
