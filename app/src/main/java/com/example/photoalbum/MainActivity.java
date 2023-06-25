package com.example.photoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rv;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> activityResultLauncherForAddImg;

    private ActivityResultLauncher<Intent> activityResultLauncherForUpdateImg;
    private  MyImagesViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        // register Activity method for Add
        registerActivityAddImg();

        // Register for update Activity
        registerforUpate();


        MyImagesAdapter myImagesAdapter = new MyImagesAdapter();
        rv.setAdapter(myImagesAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyImagesViewModel.class);

        viewModel.getAllImages().observe(MainActivity.this, new Observer<List<MyImagesModel>>() {
            @Override
            public void onChanged(List<MyImagesModel> myImagesModels) {

                myImagesAdapter.setImagesList(myImagesModels);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,AddImgActivity.class);
                activityResultLauncherForAddImg.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                viewModel.delete(myImagesAdapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(rv);


        myImagesAdapter.setListener(new MyImagesAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(MyImagesModel model) {
                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("id",model.getImgId());
                intent.putExtra("title",model.getImgTitle());
                intent.putExtra("descriiption",model.getImgDiscription());
                intent.putExtra("img",model.getImage());

                // ActivityFor result update
                activityResultLauncherForUpdateImg.launch(intent);
            }
        });
    }

    public void registerActivityAddImg(){

        activityResultLauncherForAddImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode==RESULT_OK && data !=null){

                            String title  = data.getStringExtra("title");
                            String discription = data.getStringExtra("discription");
                            byte[] img = data.getByteArrayExtra("img");

                            MyImagesModel myImages = new MyImagesModel(title,discription,img);
                            viewModel.insert(myImages);
                        }
                    }
                });
    }

    public void registerforUpate(){

        activityResultLauncherForUpdateImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if(resultCode==RESULT_OK && data !=null){

                    String title  = data.getStringExtra("title");
                    String discription = data.getStringExtra("discription");
                    byte[] img = data.getByteArrayExtra("img");
                    int id = data.getIntExtra("id",-1);

                    MyImagesModel myImages = new MyImagesModel(title,discription,img);
                    myImages.setImgId(id);
                    viewModel.update(myImages);
                }
            }
        });

    }
}