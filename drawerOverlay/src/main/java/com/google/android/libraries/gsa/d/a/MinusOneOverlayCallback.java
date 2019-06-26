package com.google.android.libraries.gsa.d.a;

import android.content.res.Configuration;
import android.os.Message;

import com.google.android.C;

import java.io.PrintWriter;

import io.fabianterhorst.server.LogTool;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.android.C.MSG_END_SCROLL;
import static com.google.android.C.MSG_ON_SCROLL;
import static com.google.android.C.MSG_START_SCROLL;

public final class MinusOneOverlayCallback extends OverlayControllerCallback {

    private final OverlaysController overlaysController;

    MinusOneOverlayCallback(OverlaysController overlaysControllerVar, OverlayControllerBinder overlayControllerBinderVar) {
        super(overlayControllerBinderVar, C.Callback_Minus);
        overlaysController = overlaysControllerVar;
    }

    final OverlayController createController(Configuration configuration) {
        return overlaysController.createController(configuration, overlayControllerBinder.mServerVersion,
                overlayControllerBinder.mClientVersion);
    }

    public final void dump(PrintWriter printWriter, String str) {
        printWriter.println(String.valueOf(str).concat("MinusOneOverlayCallback"));
        super.dump(printWriter, str);
    }

    public final boolean handleMessage(Message message) {
        if (super.handleMessage(message)) {
            return true;
        }
        OverlayController overlayControllerVar;
        long when;
        switch (message.what) {
            case MSG_START_SCROLL:
                if (overlayController != null) {
                    overlayControllerVar = overlayController;
                    when = message.getWhen();
                    if (!overlayControllerVar.isPanelOpen()) {
                        SlidingPanelLayout slidingPanelLayoutVar = overlayControllerVar.slidingPanelLayout;
                        if (slidingPanelLayoutVar.panelX < slidingPanelLayoutVar.mTouchSlop) {
                            overlayControllerVar.slidingPanelLayout.setPanelX(0);
                            overlayControllerVar.mAcceptExternalMove = true;
                            overlayControllerVar.eventX = 0;
                            overlayControllerVar.slidingPanelLayout.mForceDrag = true;
                            overlayControllerVar.downTime = when - 30;
                            LogTool.log("MinusOneOverlayCallback.msg.startScroll");
                            overlayControllerVar.dispatchTouchEvent(ACTION_DOWN, overlayControllerVar.eventX, overlayControllerVar.downTime);
                            overlayControllerVar.dispatchTouchEvent(ACTION_MOVE, overlayControllerVar.eventX, when);
                        }
                    }
                }
                return true;
            case MSG_ON_SCROLL /*4*/:
                if (this.overlayController != null) {
                    overlayControllerVar = this.overlayController;
                    float floatValue = (float) message.obj;
                    when = message.getWhen();
                    if (overlayControllerVar.mAcceptExternalMove) {
                        overlayControllerVar.eventX = (int) (floatValue * ((float) overlayControllerVar.slidingPanelLayout.getMeasuredWidth()));
                        LogTool.log("MinusOneOverlayCallback.msg.onScroll. " + floatValue + ", px=" + overlayControllerVar.eventX);
                        overlayControllerVar.dispatchTouchEvent(ACTION_MOVE, overlayControllerVar.eventX, when);
                    }
                }
                return true;
            case MSG_END_SCROLL /*5*/:
                if (overlayController != null) {
                    overlayControllerVar = overlayController;
                    when = message.getWhen();
                    if (overlayControllerVar.mAcceptExternalMove) {
                        LogTool.log("MinusOneOverlayCallback.msg.endScroll");
                        overlayControllerVar.dispatchTouchEvent(ACTION_UP, overlayControllerVar.eventX, when);
                    }
                    overlayControllerVar.mAcceptExternalMove = false;
                }
                return true;
            default:
                return false;
        }
    }
}
