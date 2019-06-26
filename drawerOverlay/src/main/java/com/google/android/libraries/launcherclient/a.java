package com.google.android.libraries.launcherclient;

import android.os.Bundle;
import android.os.IInterface;
import android.view.WindowManager.LayoutParams;

public interface a extends IInterface {
    void onLifeState(int i);

    void openOverlay(int i);

    boolean isVoiceDetectionRunning();

    String getVoiceSearchLanguage();

    void windowAttached(Bundle bundle, ILauncherOverlayCallback ILauncherOverlayCallbackVar);

    void windowAttached(LayoutParams layoutParams, ILauncherOverlayCallback ILauncherOverlayCallbackVar, int i);

    boolean isVoiceDetectionRunning(byte[] bArr, Bundle bundle);

    void onScroll(float f);

    void startScroll();

    void endScroll();

    boolean cnM();

    void closeOverlay(int i);

    void windowDetached(boolean z);

    void requestVoiceDetection(boolean z);

    void onPause();

    void onResume();
}
