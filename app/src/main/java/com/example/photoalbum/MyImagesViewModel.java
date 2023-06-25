package com.example.photoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyImagesViewModel extends AndroidViewModel {

    private  MyImagesRepositry myImagesRepositry;
    private LiveData<List<MyImagesModel>> imagesList;

    public MyImagesViewModel(@NonNull Application application) {
        super(application);

        myImagesRepositry = new MyImagesRepositry(application);

        imagesList = myImagesRepositry.getAllImages();
    }


    public void insert(MyImagesModel model){
        myImagesRepositry.insert(model);
    }

    public void delete(MyImagesModel model){
        myImagesRepositry.delete(model);
    }
    public void update(MyImagesModel model){
        myImagesRepositry.update(model);
    }

    public LiveData<List<MyImagesModel>> getAllImages(){
        return imagesList;
    }
}
