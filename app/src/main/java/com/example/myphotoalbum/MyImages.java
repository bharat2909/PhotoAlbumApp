package com.example.myphotoalbum;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_images")
public class MyImages {

    @PrimaryKey(autoGenerate = true)
    public int imageID;

    public String img_title;
    public String img_desc;
    public byte[] image;

    public MyImages(String img_title, String img_desc, byte[] image) {
        this.img_title = img_title;
        this.img_desc = img_desc;
        this.image = image;
    }

    public int getImageID() {
        return imageID;
    }

    public String getImg_title() {
        return img_title;
    }

    public String getImg_desc() {
        return img_desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public void setImg_title(String img_title) {
        this.img_title = img_title;
    }

    public void setImg_desc(String img_desc) {
        this.img_desc = img_desc;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
