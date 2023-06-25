package com.example.photoalbum;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyImagesDao {

    @Insert
    void insert(MyImagesModel model);

    @Delete
    void delete(MyImagesModel model);

    @Update
    void update(MyImagesModel model);

    @Query("SELECT * FROM my_images ORDER BY imgId ASC")
    LiveData<List<MyImagesModel>> getAllImages();



}
