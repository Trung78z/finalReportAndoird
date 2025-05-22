package com.hcmus.management.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hcmus.management.R;
import com.hcmus.management.network.FoodRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateFastFoodActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int REQUEST_PERMISSIONS = 100;
    private Uri selectedImageUri;
    private ImageView ivFoodImage;
    private Button btnCreate;
    private ImageButton btnBack;
    private String currentPhotoPath;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        ivFoodImage = findViewById(R.id.ivFoodImage);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);
        requestQueue = Volley.newRequestQueue(this);

        btnSelectImage.setOnClickListener(v -> checkPermissions());
        btnCreate.setOnClickListener(v -> createNewFoodItem());
        btnBack.setOnClickListener(v -> {
            finish();
        });

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSIONS);
        } else {
            showImageSelectionDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageSelectionDialog();
            } else {
                Toast.makeText(this, "Permissions are required to select images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImageSelectionDialog() {
        String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();
        if (photoFile != null) {
            selectedImageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    photoFile
            );
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            currentPhotoPath = imageFile.getAbsolutePath();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    if (data != null && data.getData() != null) {
                        selectedImageUri = data.getData();
                        ivFoodImage.setImageURI(selectedImageUri);
                    }
                    break;
                case CAMERA_REQUEST:
                    ivFoodImage.setImageURI(selectedImageUri);
                    break;
            }
        }
    }

    private void createNewFoodItem() {
        EditText etName = findViewById(R.id.etName);
        EditText etPrice = findViewById(R.id.etPrice);
        EditText etDescription = findViewById(R.id.etDescription);

        String name = etName.getText().toString();
        String priceText = etPrice.getText().toString();
        String description = etDescription.getText().toString();

        if (validateInput(name, priceText)) {
            double price = Double.parseDouble(priceText);
            sendFoodItemToBackend(name, price, description);
        }
    }

    private boolean validateInput(String name, String price) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter food name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.isEmpty()) {
            Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendFoodItemToBackend(String name, double price, String description) {
        btnCreate.setEnabled(false);
        FoodRequest.sendFoodItem(
                this,
                selectedImageUri,
                name,
                price,
                description,
                1,
                new FoodRequest.Callback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnCreate.setEnabled(true);
                        Toast.makeText(CreateFastFoodActivity.this, "Food item created successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                        clearForm();
                    }

                    @Override
                    public void onError(String message) {
                        btnCreate.setEnabled(true);
                        Toast.makeText(CreateFastFoodActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void clearForm() {
        ((EditText) findViewById(R.id.etName)).setText("");
        ((EditText) findViewById(R.id.etPrice)).setText("");
        ((EditText) findViewById(R.id.etDescription)).setText("");
        ivFoodImage.setImageResource(R.drawable.image_placeholder);
        selectedImageUri = null;
    }
}