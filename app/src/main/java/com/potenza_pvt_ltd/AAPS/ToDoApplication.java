package com.potenza_pvt_ltd.AAPS;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Kushagr_Jolly on 6/4/2016.
 */
public class ToDoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}