package com.example.myphotoalbum;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Entity;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.ConcurrentModificationException;

@Database(entities = {MyImages.class},version=1)
public abstract class MyImagesDatabase extends RoomDatabase {

    private static MyImagesDatabase instance;
    public abstract MyImagesDao myImagesDao();

    public static synchronized MyImagesDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                                            MyImagesDatabase.class,"my_images_database")
                                            .fallbackToDestructiveMigration()
                                            .build();
        }
        return instance;
    }



}
