package com.example.photoalbum;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepositry {

    private  MyImagesDao myImagesDao;
    private LiveData<List<MyImagesModel>> imagesList;

    // ExecutorService service class on the behalf of the AsyncTask class we created there after api 13 Async is depricated

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    public MyImagesRepositry(Application application){

        MyImagesDatabase database =  MyImagesDatabase.getInstance(application);

        myImagesDao = database.myImagesDao();

        imagesList = myImagesDao.getAllImages();

    }

    public void insert(MyImagesModel model){

        //new InsertImageAsyncTask(myImagesDao).equals(model);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.insert(model);
            }
        });
    }
    public void delete(MyImagesModel model){
       // new deleteImageAsyncTask(myImagesDao).execute(model);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.delete(model);
            }
        });
    }
    public void update(MyImagesModel model){
       // new updateImageAsyncTask(myImagesDao).execute(model);

        myImagesDao.update(model);
    }

    public LiveData<List<MyImagesModel>> getAllImages(){
        return imagesList;
    }

    // class for background operation
    // first myimagemodel is for the task for background
    // second  onprogress updated
    // third return the type of doInbackground
//    public static  class  InsertImageAsyncTask extends AsyncTask<MyImagesModel,Void,Void>{
//
//        MyImagesDao myImagesDao;
//
//        public InsertImageAsyncTask(MyImagesDao myImagesDao) {
//            this.myImagesDao = myImagesDao;
//        }
//
//        @Override
//        protected Void doInBackground(MyImagesModel... myImagesModels) {
//
//            myImagesDao.insert(myImagesModels[0]);
//
//            return null;
//        }
//    }
//    public static  class  deleteImageAsyncTask extends AsyncTask<MyImagesModel,Void,Void>{
//
//        MyImagesDao myImagesDao;
//
//        public deleteImageAsyncTask(MyImagesDao myImagesDao) {
//            this.myImagesDao = myImagesDao;
//        }
//
//        @Override
//        protected Void doInBackground(MyImagesModel... myImagesModels) {
//
//            myImagesDao.delete(myImagesModels[0]);
//
//            return null;
//        }
//    }
//    public static  class  updateImageAsyncTask extends AsyncTask<MyImagesModel,Void,Void>{
//
//        MyImagesDao myImagesDao;
//
//        public updateImageAsyncTask(MyImagesDao myImagesDao) {
//            this.myImagesDao = myImagesDao;
//        }
//
//        @Override
//        protected Void doInBackground(MyImagesModel... myImagesModels) {
//
//            myImagesDao.delete(myImagesModels[0]);
//
//            return null;
//        }
//    }
}
