package com.google.android.libraries.gsa.d.a;

import android.content.ComponentName;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.google.android.C;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import java.io.PrintWriter;

abstract class OverlayControllerCallback extends BaseCallback {

    final OverlayControllerBinder overlayControllerBinder;
    private final int callbackFlag;
    OverlayController overlayController;

    abstract OverlayController createController(Configuration configuration);

    OverlayControllerCallback(OverlayControllerBinder overlayControllerBinderVar, int flag) {
        this.overlayControllerBinder = overlayControllerBinderVar;
        this.callbackFlag = flag;
    }

    public boolean handleMessage(Message message) {
        boolean z = false;
        OverlayController overlayControllerVar;
        switch (message.what) {
            case C.MSG_WINDOW:
                if (message.arg1 == C.ARG1_WINDOW_DETACH) {
                    return true;
                }
                OverlayController overlayControllerVar2;
                Bundle bundle;
                if (overlayController != null) {//保存之前的.并销魂之前的overlay
                    overlayControllerVar2 = overlayController;
                    Bundle bundle2 = new Bundle();
                    if (overlayControllerVar2.panelState == PanelState.OPEN_AS_DRAWER) {//Todo: PanelState.uog was default
                        bundle2.putBoolean("open", true);
                    }
                    bundle2.putParcelable("view_state", overlayControllerVar2.window.saveHierarchyState());
                    this.overlayController.destroy();
                    this.overlayController = null;
                    bundle = bundle2;
                } else {
                    bundle = null;
                }
                Pair pair = (Pair) message.obj;
                LayoutParams layoutParams = ((Bundle) pair.first).getParcelable("layout_params");
                overlayController = createController((Configuration) ((Bundle) pair.first).getParcelable("configuration"));
                try {
                    OverlayController overlayControllerVar3 = this.overlayController;
                    String str = this.overlayControllerBinder.mPackageName;
                    Bundle bundle3 = (Bundle) pair.first;
                    overlayControllerVar3.mIsRtl = SlidingPanelLayout.isRtl(overlayControllerVar3.getResources());
                    overlayControllerVar3.mPackageName = str;
                    overlayControllerVar3.window.setWindowManager(null, layoutParams.token, new ComponentName(overlayControllerVar3, overlayControllerVar3.getBaseContext().getClass()).flattenToShortString(), true);
                    overlayControllerVar3.windowManager = overlayControllerVar3.window.getWindowManager();
                    Point point = new Point();
                    overlayControllerVar3.windowManager.getDefaultDisplay().getRealSize(point);
                    overlayControllerVar3.mWindowShift = -Math.max(point.x, point.y);
                    overlayControllerVar3.slidingPanelLayout = new OverlayControllerSlidingPanelLayout(overlayControllerVar3);
                    overlayControllerVar3.container = new FrameLayout(overlayControllerVar3);
                    overlayControllerVar3.slidingPanelLayout.addContentView(overlayControllerVar3.container);
                    overlayControllerVar3.slidingPanelLayout.uoH = overlayControllerVar3.overlayControllerStateChanger;
                    layoutParams.width = -1;
                    layoutParams.height = -1;
                    layoutParams.flags |= 8650752;
                    layoutParams.dimAmount = 0.0f;
                    layoutParams.gravity = 3;
                    layoutParams.type = VERSION.SDK_INT >= 25 ? LayoutParams.TYPE_DRAWN_APPLICATION : LayoutParams.TYPE_APPLICATION;
                    layoutParams.softInputMode = 16;
                    overlayControllerVar3.window.setAttributes(layoutParams);
                    overlayControllerVar3.window.clearFlags(1048576);
                    overlayControllerVar3.onCreate(bundle3);
                    overlayControllerVar3.window.setContentView(overlayControllerVar3.slidingPanelLayout);
                    overlayControllerVar3.windowView = overlayControllerVar3.window.getDecorView();
                    overlayControllerVar3.windowManager.addView(overlayControllerVar3.windowView, overlayControllerVar3.window.getAttributes());
                    overlayControllerVar3.slidingPanelLayout.setSystemUiVisibility(1792);
                    overlayControllerVar3.setTouchable(false);
                    overlayControllerVar3.window.getDecorView().addOnLayoutChangeListener(new OverlayControllerLayoutChangeListener(overlayControllerVar3));
                    if (bundle != null) {
                        overlayControllerVar = this.overlayController;
                        overlayControllerVar.window.restoreHierarchyState(bundle.getBundle("view_state"));
                        if (bundle.getBoolean("open")) {
                            SlidingPanelLayout slidingPanelLayoutVar = overlayControllerVar.slidingPanelLayout;
                            slidingPanelLayoutVar.mPanelPositionRatio = 1.0f;
                            slidingPanelLayoutVar.panelX = slidingPanelLayoutVar.getMeasuredWidth();
                            slidingPanelLayoutVar.contentView.setTranslationX(slidingPanelLayoutVar.mIsRtl ? (float) (-slidingPanelLayoutVar.panelX) : (float) slidingPanelLayoutVar.panelX);
                            slidingPanelLayoutVar.cnF();
                            slidingPanelLayoutVar.cnG();
                        }
                    }
                    overlayControllerVar2 = this.overlayController;
                    overlayControllerVar2.callback = (ILauncherOverlayCallback) pair.second;
                    overlayControllerVar2.requestVoice(true);
                    overlayControllerBinder.replay((ILauncherOverlayCallback) pair.second, callbackFlag);
                    return true;
                } catch (Throwable e) {
                    Log.d("OverlaySController", "Error creating overlay window", e);
                    Message obtain = Message.obtain();
                    obtain.what = 2;
                    handleMessage(obtain);
                    obtain.recycle();
                    return true;
                }
            case C.MSG_LIFE_STATE:
                if (this.overlayController == null) {
                    return true;
                }
                this.overlayController.onActivityStateFlag((Integer) message.obj);
                return true;
            case C.MSG_CREATE_DESTROY:
                if (this.overlayController == null) {
                    return true;
                }
                ILauncherOverlayCallback reply = overlayController.destroy();
                this.overlayController = null;
                if (message.arg1 != 0) {
                    return true;
                }
                overlayControllerBinder.replay(reply, 0);
                return true;
            case C.MSG_TOGGLE_OVERLAY:
                if (this.overlayController == null) {
                    return true;
                }
                int value = message.arg2 & 1;
                if (message.arg1 == C.ARG1_OVERLAY_OPEN) {
                    overlayController.openOverlay(value);
                } else {
                    overlayController.closeOverlay(value);
                }
                return true;
            case C.MSG_REQUEST_VOICE:
                if (this.overlayController == null) {
                    return true;
                }
                overlayControllerVar = this.overlayController;
                if (message.arg1 == 1) {
                    z = true;
                }
                overlayControllerVar.requestVoice(z);
                return true;
            case 8://空方法
                if (overlayController != null) {
                    overlayController.a((ByteBundleHolder) message.obj);
                }
                return true;
            default:
                return false;
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        OverlayController overlayControllerVar = this.overlayController;
        String valueOf = String.valueOf(overlayControllerVar);
        printWriter.println(str + " mView: " + valueOf);
        if (overlayControllerVar != null) {
            overlayControllerVar.dump(printWriter, String.valueOf(str).concat("  "));
        }
    }
}
