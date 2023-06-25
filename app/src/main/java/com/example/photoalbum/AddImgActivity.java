package com.example.photoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImgActivity extends AppCompatActivity {

    private EditText etTitle,etDiscription;
    private ImageView img;

    Bitmap selectedImages;

    private  Bitmap scaledImg;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImg;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Image");
        setContentView(R.layout.activity_add_img);

        // register Activity
        registerActivityForSelectImg();

        etTitle = findViewById(R.id.etTitle);
        etDiscription = findViewById(R.id.etDiscription);
        img = findViewById(R.id.imgAdd);
        btnSave = findViewById(R.id.btnSave);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(AddImgActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddImgActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherForSelectImg.launch(intent);

                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedImages == null) {
                    Toast.makeText(getApplicationContext(), "Please select an images", Toast.LENGTH_SHORT).show();
                } else {
                    String title = etTitle.getText().toString();
                    String discription = etDiscription.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImg = makesmall(selectedImages, 300);
                    selectedImages.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

                    byte[] img = outputStream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("title",title);
                    intent.putExtra("discription",discription);
                    intent.putExtra("img",img);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    public void registerActivityForSelectImg(){

        activityResultLauncherForSelectImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data  = result .getData();

                        if(resultCode == RESULT_OK && data !=null){
                            try {
                                selectedImages = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                                img.setImageBitmap(selectedImages);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImg.launch(intent);
        }
    }

    public Bitmap makesmall(Bitmap img,int maxSize){
        int width = img.getWidth();
        int hight = img.getHeight();

        float ratio = (float) width / (float) hight;
        if(ratio> 1){
            width = maxSize;
            hight = (int)(width /ratio);
        }else{
            hight = maxSize;
            width = (int)(hight/ratio);
        }
        return Bitmap.createScaledBitmap(img,width,hight,true);
    }
}