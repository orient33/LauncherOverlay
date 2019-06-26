package com.google.android.libraries.launcherclient;

import android.os.IInterface;

public interface ILauncherOverlayCallback extends IInterface {
    void statusChanged(int status);

    void scrollChanged(float progress);
}
