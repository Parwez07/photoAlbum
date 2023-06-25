package com.example.photoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private EditText etTitle,etDiscription;
    private ImageView imgUpdate;
    Button btnUpdate;

    private String title, description;
    int id;
    private  byte[] img;

    Bitmap selectedImages;
    private  Bitmap scaledImg;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().setTitle("Update Image");
        setContentView(R.layout.activity_update);

        etDiscription = findViewById(R.id.etDiscription);
        etTitle = findViewById(R.id.etTitle);
        imgUpdate =findViewById(R.id.imgUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);

        // register Activity
        registerActivityForSelectImg();

        id = getIntent().getIntExtra("id",-1);

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        img = getIntent().getByteArrayExtra("img");

        etTitle.setText(title);
        etDiscription.setText(description);
        imgUpdate .setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));

        imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImg.launch(intent);

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }

    public void updateData() {

        if (id == -1) {
            Toast.makeText(getApplicationContext(), "Please select an images", Toast.LENGTH_SHORT).show();
        }
        else{
            String title = etTitle.getText().toString();
            String discription = etDiscription.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("title",title);
            intent.putExtra("discription",discription);
            intent.putExtra("id",id);

            if (selectedImages == null) {
                intent.putExtra("img",img);

            } else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImg = makesmall(selectedImages, 300);
                selectedImages.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

                byte[] img = outputStream.toByteArray();

                intent.putExtra("img",selectedImages);
            }
            setResult(RESULT_OK,intent);
            finish();

        }

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
                                imgUpdate.setImageBitmap(selectedImages);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
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