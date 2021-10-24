package com.example.alertmeapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AlertFormActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int REQUEST_EXTERNAL_STORAGE = 102;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    private TextView titleView;
    private TextView descriptionView;
    private Spinner categorySpinner;
    private ImageView uploadedPhotoView;
    private TextView photoUploadInfoView;

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> processTakenPhoto(result));

    private final ActivityResultLauncher<Intent> externalStorageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> processChosenPhoto(result));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_form);
        populateCategorySpinner();

        titleView = findViewById(R.id.alert_form_title);
        descriptionView = findViewById(R.id.alert_form_description);
        uploadedPhotoView = findViewById(R.id.uploaded_photo);
        photoUploadInfoView = findViewById(R.id.alert_image_info);

        uploadedPhotoView.setVisibility(View.INVISIBLE);
    }

    private void populateCategorySpinner() {
        categorySpinner = findViewById(R.id.alert_form_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getCategoriesFromServer());
        categorySpinner.setAdapter(adapter);
    }

    private String[] getCategoriesFromServer() {
        //TODO: replace with request to server
        return new String[]{"information", "warning", "other"};
    }

    public void onFormUploadClick(View view) {
        String title = titleView.getText().toString();
        String description = descriptionView.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();

        //TODO: come up with a way of uploading photo
        //TODO: upload form to the server
    }

    public void onChoosePhotoClick(View view) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        externalStorageActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            uploadedPhotoView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void onTakePhotoClick(View view) {
        if (!checkCameraHardware(view.getContext())) {
            //TODO: inform that system has no camera
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_CAMERA, CAMERA_REQUEST_CODE);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(intent);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void processChosenPhoto(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            uploadedPhotoView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            showUploadedPhoto();
        }
    }

    private void processTakenPhoto(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadedPhotoView.setImageBitmap(imageBitmap);
            showUploadedPhoto();
        }
    }

    private void showUploadedPhoto() {
        photoUploadInfoView.setVisibility(View.INVISIBLE);
        uploadedPhotoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: Camera permission granted
            } else {
                //TODO: Camera permission denied
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: External storage permission granted
            } else {
                //TODO: External storage permission denied
            }
        }
    }
}