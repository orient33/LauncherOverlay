package com.google.android.apps.gsa.nowoverlayservice;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;

import com.google.android.libraries.gsa.d.a.OverlayController;
import com.google.android.libraries.gsa.d.a.OverlaysController;
import com.google.android.libraries.gsa.d.a.v;

import javax.annotation.Nullable;

public final class ConfigurationOverlayController extends OverlaysController {

    private final Context mContext;

    ConfigurationOverlayController(Service service) {
        super(service);
        this.mContext = service;
    }

    //Todo: was protected
    public final int flag() {
        return 24;
    }

    public final OverlayController createController(@Nullable Configuration configuration,
                                                    int serviceVersion, int clientVer) {
        Context context = this.mContext;
        if (configuration != null) {
            context = context.createConfigurationContext(configuration);
        }
        return new Overlay(context, serviceVersion, clientVer);
    }

    //Todo: was protected, and return modified
    public final v HA() {
        return new v();
    }
}
