package com.google.android.libraries.gsa.d.a;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import java.io.PrintWriter;

import io.fabianterhorst.server.LogTool;

public class OverlayController extends DialogOverlayController {

    public boolean mIsRtl;
    public long downTime = 0;
    public int mWindowShift;
    public String mPackageName;
    public SlidingPanelLayout slidingPanelLayout;
    public t overlayControllerStateChanger = new OverlayControllerStateChanger(this);
    public FrameLayout container;
    public int eventX = 0;
    public boolean mAcceptExternalMove = false;
    public boolean unZ = true;
    public ILauncherOverlayCallback callback;
    public PanelState panelState = PanelState.CLOSED;
    public int mActivityStateFlags = 0;

    public OverlayController(Context context, int theme, int dialogTheme) {
        super(context, theme, dialogTheme);
    }

    final void dispatchTouchEvent(int action, int x, long eventTime) {
        MotionEvent obtain = MotionEvent.obtain(downTime, eventTime, action,
                mIsRtl ? (float) (-x) : (float) x, 0.0f, 0);
        obtain.setSource(InputDevice.SOURCE_TOUCHSCREEN);//4098
        slidingPanelLayout.dispatchTouchEvent(obtain);
        obtain.recycle();
    }

    final ILauncherOverlayCallback destroy() {
        onActivityStateFlag(0);
        try {
            this.windowManager.removeView(this.windowView);
        } catch (Throwable e) {
            Log.e("wo.OverlayController", "Error removing overlay window", e);
        }
        this.windowView = null;
        dismiss();
        onDestroy();
        return callback;
    }

    final void onActivityStateFlag(int stateFlag) {
        LogTool.log("OverlayController.onActivityStateFlag. " + stateFlag);
        int i2 = 1;
        int i3 = 0;
        if (this.mActivityStateFlags != stateFlag) {
            int i4;
            int i5;
            int i6;
            int i7 = (this.mActivityStateFlags & 1) != 0 ? 1 : 0;
            if ((this.mActivityStateFlags & 2) != 0) {
                i4 = 1;
            } else {
                i4 = 0;
            }
            if ((stateFlag & 1) != 0) {
                i5 = 1;
            } else {
                i5 = 0;
            }
            if ((stateFlag & 2) != 0) {
                i6 = 1;
            } else {
                i6 = 0;
            }
            if (i5 == 0 && i6 == 0) {
                i5 = 0;
            } else {
                i5 = 1;
            }
            if (i5 == 0) {
                i2 = 0;
            }
            if (i6 != 0) {
                i3 = 2;
            }
            this.mActivityStateFlags = i2 | i3;
            if (i7 == 0 && i5 != 0) {
                onStart();
            }
            if (i4 == 0 && i6 != 0) {
                onResume();
            }
            if (i4 != 0 && i6 == 0) {
                onPause();
            }
            if (i7 != 0 && i5 == 0) {
                onStop();
            }
        }
    }

    public void closeOverlay(int i) {
        LogTool.log("OverlayController.closeOverlay " + i);
        int i2 = 1;
        int i3 = 0;
        if (isPanelOpen()) {
            int i4 = (i & 1) != 0 ? 1 : 0;
            if (this.panelState == PanelState.OPEN_AS_LAYER) {
                i2 = 0;
            }
            i4 &= i2;
            SlidingPanelLayout slidingPanelLayoutVar = this.slidingPanelLayout;
            if (i4 != 0) {
                i3 = 750;
            }
            slidingPanelLayoutVar.closePanel(i3);
            dismiss();
        }
    }

    public final void openOverlay(int i) {
        LogTool.log("OverlayController. openOverlay " + i);
        int i2 = 0;
        if (this.panelState == PanelState.CLOSED) {
            int i3 = (i & 1) != 0 ? 1 : 0;
            if ((i & 2) != 0) {
                this.slidingPanelLayout.uoH = new TransparentOverlayController(this);
                i3 = 0;
            }
            SlidingPanelLayout slidingPanelLayoutVar = this.slidingPanelLayout;
            if (i3 != 0) {
                i2 = 750;
            }
            slidingPanelLayoutVar.fv(i2);
        }
    }

    public void a(ByteBundleHolder byteBundleHolderVar) {
    }

    public void onBackPressed() {
        closeOverlay(1);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "mWindowShift: " + this.mWindowShift);
        printWriter.println(str + "mAcceptExternalMove: " + this.mAcceptExternalMove);
        String valueOf = String.valueOf(this.panelState);
        printWriter.println(str + "mDrawerState: " + valueOf);
        printWriter.println(str + "mActivityStateFlags: " + this.mActivityStateFlags);
        valueOf = String.valueOf(this.slidingPanelLayout);
        printWriter.println(str + "mWrapperView: " + valueOf);
        SlidingPanelLayout slidingPanelLayoutVar = this.slidingPanelLayout;
        String concat = String.valueOf(str).concat("  ");
        printWriter.println(concat + "mPanelPositionRatio: " + slidingPanelLayoutVar.mPanelPositionRatio);
        printWriter.println(concat + "mDownX: " + slidingPanelLayoutVar.mDownX);
        printWriter.println(concat + "mDownY: " + slidingPanelLayoutVar.mDownY);
        printWriter.println(concat + "mActivePointerId: " + slidingPanelLayoutVar.mActivePointerId);
        printWriter.println(concat + "mTouchState: " + slidingPanelLayoutVar.mTouchState);
        printWriter.println(concat + "mIsPanelOpen: " + slidingPanelLayoutVar.mIsPanelOpen);
        printWriter.println(concat + "mIsPageMoving: " + slidingPanelLayoutVar.mIsPageMoving);
        printWriter.println(concat + "mSettling: " + slidingPanelLayoutVar.mSettling);
        printWriter.println(concat + "mForceDrag: " + slidingPanelLayoutVar.mForceDrag);
    }

    public void Hn() {
    }

    public void onCreate(Bundle bundle) {
        LogTool.log("OverlayController. onCreate.");
    }

    public void onPause() {
        LogTool.log("OverlayController. onPause");
    }

    public void onStop() {
        LogTool.log("OverlayController. onStop");
    }

    public void onStart() {
        LogTool.log("OverlayController. onStart");
    }

    public void onResume() {
        LogTool.log("OverlayController. onResume");
    }

    public Window getWindow() {
        return this.window;
    }

    public final void setTitle(CharSequence charSequence) {
        this.window.setTitle(charSequence);
    }

    public void onDestroy() {
        LogTool.log("OverlayController. onDestroy");
    }

    public void requestVoice(boolean z) {
    }

    public boolean Ho() {
        return false;
    }

    public void afterReply(float f) {
    }

    public Object getSystemService(String str) {
        if (!"window".equals(str) || this.windowManager == null) {
            return super.getSystemService(str);
        }
        return this.windowManager;
    }

    public final boolean isPanelOpen() {
        return panelState == PanelState.OPEN_AS_DRAWER || panelState == PanelState.OPEN_AS_LAYER;
    }

    public final void setTouchable(boolean visible) {
        LogTool.log("OverlayController.setTouchable. " + visible);
        final int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        if (visible) {
            window.clearFlags(flag);
        } else {
            window.addFlags(flag);
        }
    }

    public void setState(PanelState panelStateVar) {
    }
}
