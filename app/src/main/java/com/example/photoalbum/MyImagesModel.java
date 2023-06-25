package com.example.photoalbum;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_images")
public class MyImagesModel {

    @PrimaryKey(autoGenerate = true)
    int imgId;

    String imgTitle;

    String imgDiscription;

    // multimidea formate are save in blob ... pass the array of bytes

    public byte[] image;

    public MyImagesModel( String imgTitle, String imgDiscription, byte[] image) {
        this.imgTitle = imgTitle;
        this.imgDiscription = imgDiscription;
        this.image = image;
    }

    public int getImgId() {
        return imgId;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public String getImgDiscription() {
        return imgDiscription;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
