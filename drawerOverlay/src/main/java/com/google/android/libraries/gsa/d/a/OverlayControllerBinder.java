package com.google.android.libraries.gsa.d.a;

import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager.LayoutParams;

import com.google.android.C;
import com.google.android.libraries.launcherclient.LauncherOverlayInterfaceBinder;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import static com.google.android.C.ARG1_OVERLAY_CLOSE;
import static com.google.android.C.ARG1_OVERLAY_OPEN;
import static com.google.android.C.ARG1_WINDOW_ATTACH;
import static com.google.android.C.ARG1_WINDOW_DETACH;
import static com.google.android.C.MSG_CREATE_DESTROY;
import static com.google.android.C.MSG_END_SCROLL;
import static com.google.android.C.MSG_LIFE_STATE;
import static com.google.android.C.MSG_REQUEST_VOICE;
import static com.google.android.C.MSG_START_SCROLL;
import static com.google.android.C.MSG_ON_SCROLL;
import static com.google.android.C.MSG_TOGGLE_OVERLAY;
import static com.google.android.C.MSG_WINDOW;

final class OverlayControllerBinder extends LauncherOverlayInterfaceBinder implements Runnable {

    int mOptions = 0;
    final String mPackageName;
    private final OverlaysController overlaysController;
    final int mCallerUid;
    final int mServerVersion;
    final int mClientVersion;
    BaseCallback baseCallback = new BaseCallback();
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper(), baseCallback);
    boolean mLastAttachWasLandscape;

    OverlayControllerBinder(OverlaysController overlaysControllerVar, int callerUid, String packageName, int serverVersion, int clientVersion) {
        overlaysController = overlaysControllerVar;
        mCallerUid = callerUid;
        mPackageName = packageName;
        mServerVersion = serverVersion;
        mClientVersion = clientVersion;
    }

    private void checkCallerId() {
        if (Binder.getCallingUid() != mCallerUid) {
            //throw new RemoteException("Invalid client");
            throw new RuntimeException("Invalid client");//FIXME: modified, was remote exception and should still be one, realy have to change that
        }
    }

    public final synchronized void startScroll() {
        checkCallerId();
        Message.obtain(mainThreadHandler, MSG_START_SCROLL).sendToTarget();
    }

    public final synchronized void onScroll(float f) {
        checkCallerId();
        Message.obtain(mainThreadHandler, MSG_ON_SCROLL, f).sendToTarget();
    }

    public final synchronized void endScroll() {
        checkCallerId();
        Message.obtain(mainThreadHandler, MSG_END_SCROLL).sendToTarget();
    }

    @Override
    public final synchronized void windowAttached(LayoutParams layoutParams, ILauncherOverlayCallback callback, int clientOptions) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("layout_params", layoutParams);
        bundle.putInt("client_options", clientOptions);
        windowAttached(bundle, callback);
    }

    @Override
    public final synchronized void windowAttached(Bundle bundle, ILauncherOverlayCallback callback) {
        checkCallerId();
        overlaysController.handler.removeCallbacks(this);
        Configuration configuration = bundle.getParcelable("configuration");
        mLastAttachWasLandscape = configuration != null && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
        create(bundle.getInt("client_options", 7));
        Message.obtain(mainThreadHandler, MSG_WINDOW, ARG1_WINDOW_ATTACH, 0, Pair.create(bundle, callback)).sendToTarget();
    }

    @Override
    public final synchronized void windowDetached(boolean delay) {
        checkCallerId();
        Message.obtain(mainThreadHandler, MSG_WINDOW, ARG1_WINDOW_DETACH, 0).sendToTarget();
        overlaysController.handler.postDelayed(this, delay ? 5000 : 0);
    }

    @Override
    public final synchronized void onLifeState(int i) {// 0-pause.. 2-?  3-resume
        checkCallerId();
        mainThreadHandler.removeMessages(MSG_LIFE_STATE);
        if ((i & 2) == 0) {
            mainThreadHandler.sendMessageDelayed(Message.obtain(mainThreadHandler, MSG_LIFE_STATE, i), 100);
        } else {
            Message.obtain(mainThreadHandler, MSG_LIFE_STATE, i).sendToTarget();
        }
    }

    public final synchronized void onPause() {
        onLifeState(C.LIFE_PAUSE);
    }

    private synchronized void create(int clientOption) {
        synchronized (this) {
            int i2 = clientOption & 0xF; // 15: 0xf
            if ((i2 & 1) != 0) {
                i2 = 1;
            }
            if (mOptions != i2) {
                BaseCallback baseCallbackVar;
                mainThreadHandler.removeCallbacksAndMessages(null);
                Message.obtain(mainThreadHandler, MSG_WINDOW, ARG1_WINDOW_DETACH, 0).sendToTarget();
                createOrDestroy(true);
                mOptions = i2;
                if (mOptions == 1) {
                    baseCallbackVar = new MinusOneOverlayCallback(overlaysController, this);
                } else {
                    baseCallbackVar = new BaseCallback();
                }
                baseCallback = baseCallbackVar;
                mainThreadHandler = new Handler(Looper.getMainLooper(), baseCallback);
            }
        }
    }

    public final synchronized void onResume() {
        onLifeState(C.LIFE_RESUME);
    }

    @Override
    public final synchronized void closeOverlay(int i) {
        checkCallerId();
        mainThreadHandler.removeMessages(MSG_TOGGLE_OVERLAY);
        Message.obtain(mainThreadHandler, MSG_TOGGLE_OVERLAY, ARG1_OVERLAY_CLOSE, i).sendToTarget();
    }

    @Override
    public final synchronized void openOverlay(int i) {
        checkCallerId();
        mainThreadHandler.removeMessages(MSG_TOGGLE_OVERLAY);
        Message.obtain(mainThreadHandler, MSG_TOGGLE_OVERLAY, ARG1_OVERLAY_OPEN, i).sendToTarget();
    }

    @Override
    public final synchronized boolean isVoiceDetectionRunning(byte[] bArr, Bundle bundle) {
        Message.obtain(mainThreadHandler, 8, new ByteBundleHolder(bArr, bundle)).sendToTarget();
        return true;
    }

    @Override
    public final synchronized void requestVoiceDetection(boolean z) {
        synchronized (this) {
            checkCallerId();
            mainThreadHandler.removeMessages(MSG_REQUEST_VOICE);
            Message.obtain(mainThreadHandler, MSG_REQUEST_VOICE, z ? 1 : 0, 0).sendToTarget();
        }
    }

    @Override
    public final String getVoiceSearchLanguage() {
        return overlaysController.HA().HB();
    }

    @Override
    public final boolean isVoiceDetectionRunning() {
        return overlaysController.HA().HC();
    }

    //Todo: always true, remove
    public final boolean cnM() {
        return true;
    }

    public final void run() {
        destroy();
    }

    final void destroy() {
        synchronized (overlaysController) {
            overlaysController.handler.removeCallbacks(this);
            createOrDestroy(false);
        }
    }

    private synchronized void createOrDestroy(boolean create) {
        synchronized (this) {
            Handler handler = mainThreadHandler;
            Message.obtain(handler, MSG_CREATE_DESTROY, create ? 1 : 0, 0).sendToTarget();
        }
    }

    final void replay(ILauncherOverlayCallback ILauncherOverlayCallbackVar, int flag) {
        if (ILauncherOverlayCallbackVar != null) {
            try {
                ILauncherOverlayCallbackVar.statusChanged(overlaysController.flag() | flag);
            } catch (Throwable e) {
                Log.e("OverlaySController", "Failed to send status update", e);
            }
        }
    }
}
