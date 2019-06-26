package com.google.android.apps.gsa.nowoverlayservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.libraries.gsa.d.a.OverlaysController;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DrawerOverlayService extends Service {

    private OverlaysController overlaysController;

    @Override
    public void onCreate() {
        super.onCreate();
        overlaysController = new ConfigurationOverlayController(this);
    }

    @Override
    public void onDestroy() {
        overlaysController.onDestroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return overlaysController.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        overlaysController.unUnbind(intent);
        return false;
    }

    @Override
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        overlaysController.dump(printWriter);
    }
}
