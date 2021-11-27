package com.example.alertmeapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.alertmeapp.R;
import com.example.alertmeapp.activities.MapsActivity;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.utils.LoggedInUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertFormFragment extends Fragment {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int REQUEST_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_LOCATION_CODE = 103;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };
    private static String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private final String INVALID_TITLE = "Title cannot be empty";
    private final String INVALID_DESCRIPTION = "Description cannot be empty";
    private final String INVALID_LOCALIZATION = "Please enter the localization";

    private EditText titleView;
    private TextView titleInvalidView;
    private EditText descriptionView;
    private TextView descriptionInvalidView;
    private Spinner categorySpinner;
    private ImageView uploadedPhotoView;
    private TextView photoUploadInfoView;
    private ConstraintLayout photoUploadLayout;
    private TextView localizationInvalid;

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> processTakenPhoto(result));

    private final ActivityResultLauncher<Intent> externalStorageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> processChosenPhoto(result));

    private Double longitude;
    private Double latitude;
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private List<AlertType> alertTypeRequests = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_form, container, false);

        Button buttonPhoto = view.findViewById(R.id.take_photo_btn);
        Button buttonUploader = view.findViewById(R.id.upload_form);
        Button buttonPhotoChooser = view.findViewById(R.id.choose_photo_button);
        Button buttonLocalization = view.findViewById(R.id.enter_localization);

        buttonPhoto.setOnClickListener(this::onTakePhotoClick);
        buttonUploader.setOnClickListener(this::onCheckDuplicates);
        buttonPhotoChooser.setOnClickListener(this::onChoosePhotoClick);
        buttonLocalization.setOnClickListener(this::onChooseLocalization);

        categorySpinner = view.findViewById(R.id.alert_form_category);
        populateCategorySpinner();

        titleView = view.findViewById(R.id.alert_form_title);
        titleInvalidView = view.findViewById(R.id.alert_form_title_invalid);
        descriptionView = view.findViewById(R.id.alert_form_description);
        descriptionInvalidView = view.findViewById(R.id.alert_form_description_invalid);
        uploadedPhotoView = view.findViewById(R.id.uploaded_photo);
        photoUploadInfoView = view.findViewById(R.id.alert_image_info);
        photoUploadLayout = view.findViewById(R.id.photo_upload_constraint);
        localizationInvalid = view.findViewById(R.id.enter_localization_invalid);

        //Hide photo upload section if camera is not available
        if (!checkCameraHardware(getActivity())) {
            photoUploadLayout.setVisibility(View.INVISIBLE);
        } else {
            uploadedPhotoView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        getNewAlertCords();
    }

    private void getNewAlertCords() {
        //za kazdymr azem a nie po wybraniu
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        this.longitude = Double.valueOf(sharedPref.getFloat("longitude", 51.759f));
        this.latitude = Double.valueOf(sharedPref.getFloat("latitude", 19.457f));

    }

    private void populateCategorySpinner() {
        List<String> categories = new ArrayList<>(5);

        Call<ResponseMultipleData<AlertType>> allAlertTypes = service.getAllAlertTypes();
        allAlertTypes.enqueue(new Callback<ResponseMultipleData<AlertType>>() {
            @Override
            public void onResponse(Call<ResponseMultipleData<AlertType>> call, Response<ResponseMultipleData<AlertType>> response) {
                for (AlertType alertTypeRequest : response.body().getData()) {
                    categories.add(alertTypeRequest.getName());
                    alertTypeRequests.add(alertTypeRequest);
                }
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<AlertType>> call, Throwable t) {
                displayToast("Error occurred");
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }
        });
    }

    private boolean isNewAlertValid() {
        if (longitude != null && latitude != null) {
            return true;
        }
        return false;
    }

    public void onCheckDuplicates(View view) {

        if (isNewAlertValid()) {
            Bundle bundle = new Bundle();
            String category = categorySpinner.getSelectedItem().toString();
            bundle.putDouble("longitude", longitude);
            bundle.putDouble("latitude", latitude);
            bundle.putLong("alertTypeId", getSelectedCategoryAlertType(category).getId() == null
                    ? alertTypeRequests.get(0).getId() : getSelectedCategoryAlertType(category).getId());
            NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
            navController.navigate(R.id.duplicateFragment, bundle);
        } else {
            System.out.println("nie ustawiels lokalizacji byczqu");
        }

//        String title = titleView.getText().toString();
//        String description = descriptionView.getText().toString();
//        String category = categorySpinner.getSelectedItem().toString();
//        boolean titleValid = validateTitle(title);
//        boolean descriptionValid = validateDescription(description);
//
//        if (!titleValid) {
//            titleInvalidView.setText(INVALID_TITLE);
//        } else {
//            titleInvalidView.setText("");
//        }
//
//        if (!descriptionValid) {
//            titleInvalidView.setText(INVALID_TITLE);
//            descriptionInvalidView.setText(INVALID_DESCRIPTION);
//        } else {
//            descriptionInvalidView.setText("");
//        }
//
//        if (longitude == null || latitude == null) {
//            localizationInvalid.setText(INVALID_LOCALIZATION);
//        } else {
//            localizationInvalid.setText("");
//        }
//
//        if (titleValid && descriptionValid && longitude != null && latitude != null) {
//            AlertRequest alertRequest = new AlertRequest.Builder()
//                    .withUserId(LoggedInUser.getInstance(null,null,null).getId())
//                    .withAlertTypeId(getSelectedCategoryAlertType(category).getId())
//                    .withDescription(description)
//                    .withTitle(title)
//                    .withLatitude(latitude)
//                    .withLongitude(longitude)
//                    .withNumberOfVotes(0)
//                    .withImage(getUploadedPhotoBytesArray())
//                    .build();
//            requestToSaveAlert(alertRequest);
//            requestToSaveAlert(new AlertRequest(Long.valueOf(LoggedInUser.getInstance(null,null,null).getId()), Long.valueOf(getSelectedCategoryAlertType(category).getId())
//                    , title, description, 0, latitude, longitude, getCurrentDate(), getUploadedPhotoBytesArray()));
//        }
    }

    private void requestToSaveAlert(AlertRequest alertRequest) {
        Call<ResponseBody> responseBodyCall = service.saveNewAlert(alertRequest);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
                    navController.navigate(R.id.mapsFragment);
                    displayToast("Alert added");
                } else {
                    displayToast("Error on save new alert");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                displayToast("Error on save new alert");
            }
        });
    }

    private String getUploadedPhotoBytesArray() {
        BitmapDrawable drawable = (BitmapDrawable) uploadedPhotoView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }

    private void clearInputs() {
        titleView.setText("");
        descriptionView.setText("");
        latitude = null;
        longitude = null;
        uploadedPhotoView.setImageBitmap(null);
    }


    public void onChoosePhotoClick(View view) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        externalStorageActivityResultLauncher.launch(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            uploadedPhotoView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void onTakePhotoClick(View view) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_CAMERA, CAMERA_REQUEST_CODE);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(intent);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void processChosenPhoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
        if (result.getResultCode() == RESULT_OK) {
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

    private boolean validateTitle(String title) {
        return !title.isEmpty();
    }

    private boolean validateDescription(String description) {
        return !description.isEmpty();
    }

    public void onChooseLocalization(View view) {
        Intent i = new Intent(getActivity(), MapsActivity.class);
        i.putExtra("longitude", LoggedInUser.getLoggedUser().getLastLongitude());
        i.putExtra("latitude", LoggedInUser.getLoggedUser().getLastLatitude());
        startActivity(i);
    }

    private void displayToast(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }

    private String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOnly = dateFormat.format(currentDate);
        return dateOnly;
    }

    private AlertType getSelectedCategoryAlertType(String category) {
        AlertType selectedCategory = alertTypeRequests.stream()
                .filter(alertTypeRequest -> alertTypeRequest.getName() == category)
                .findFirst()
                .get();
        return selectedCategory;
    }
}