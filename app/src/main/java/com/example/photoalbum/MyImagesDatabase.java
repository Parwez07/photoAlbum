package com.example.photoalbum;

import android.content.Context;
import android.content.pm.VersionedPackage;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MyImagesModel.class},version = 1)
public abstract class MyImagesDatabase extends RoomDatabase {

    public static MyImagesDatabase instance;

    public abstract MyImagesDao myImagesDao();

    public  static synchronized  MyImagesDatabase getInstance(Context context){

        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyImagesDatabase.class,"my_images_database")
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries() // not reccomded one
                    .build();
        }

        return instance;
    }
}
