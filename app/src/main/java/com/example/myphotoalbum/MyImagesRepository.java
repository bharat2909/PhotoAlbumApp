package com.example.myphotoalbum;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDao myimagesDao;
    public LiveData<List<MyImages>> imageList;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyImagesRepository(Application application)
    {
        MyImagesDatabase database = MyImagesDatabase.getInstance(application.getApplicationContext());
        myimagesDao = database.myImagesDao();
        imageList = myimagesDao.getAllImages();

    }

    public void insert(MyImages myImages){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.insert(myImages);
            }
        });
    }

    public void delete(MyImages myImages){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.delete(myImages);
            }
        });
    }

    public void update(MyImages myImages){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.update(myImages);
            }
        });
    }

    public LiveData<List<MyImages>> getALlImages(){
        return imageList;
    }
}
