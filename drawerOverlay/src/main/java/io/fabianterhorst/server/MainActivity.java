package io.fabianterhorst.server;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//from https://www.apkmirror.com/apk/google-inc/google-search/google-search-7-10-30-release/google-app-7-10-30-21-arm64-android-apk-download/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}