package com.example.myapplication;
import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class Firebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
