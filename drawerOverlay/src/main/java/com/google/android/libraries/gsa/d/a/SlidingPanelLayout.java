package com.google.android.libraries.gsa.d.a;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class SlidingPanelLayout extends FrameLayout {

    public static boolean DEBUG = true;//Todo: debug const is here
    public static boolean uoK = false;
    public static boolean uoL = false;
    public static final Property PANEL_X = new SlidingPanelLayoutProperty(Integer.class, "panelX");
    public float mDownX;
    public float mDownY;
    public int mActivePointerId = -1;
    public float mDensity;
    public int mFlingThresholdVelocity;
    public boolean mIsPageMoving = false;
    public final boolean mIsRtl;
    public float mLastMotionX;
    public int mMaximumVelocity;
    public int mMinFlingVelocity;
    public int mMinSnapVelocity;
    public float mTotalMotionX;
    public int mTouchSlop;
    public int mTouchState = 0;
    public VelocityTracker mVelocityTracker;
    public View contentView;
    public View uoB;
    public int panelX;
    public float mPanelPositionRatio;
    public float uoE;
    public float uoF;
    public SlidingPanelLayoutInterpolator slidingPanelLayoutInterpolator;
    public t uoH;
    public boolean mIsPanelOpen = false;
    public boolean mForceDrag;
    public boolean mSettling;
    public DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(3.0f);

    public SlidingPanelLayout(Context context) {
        super(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mDensity = getResources().getDisplayMetrics().density;
        this.mFlingThresholdVelocity = (int) (500.0f * this.mDensity);
        this.mMinFlingVelocity = (int) (250.0f * this.mDensity);
        this.mMinSnapVelocity = (int) (1500.0f * this.mDensity);
        this.slidingPanelLayoutInterpolator = new SlidingPanelLayoutInterpolator(this);
        this.mIsRtl = isRtl(getResources());
    }

    public final void addContentView(View view) {
        contentView = view;
        super.addView(contentView);
    }

    final void setPanelX(int i) {
        if (i <= 1) {
            i = 0;
        }
        int measuredWidth = getMeasuredWidth();
        this.mPanelPositionRatio = ((float) i) / ((float) measuredWidth);
        this.panelX = Math.max(Math.min(i, measuredWidth), 0);
        this.contentView.setTranslationX(this.mIsRtl ? (float) (-this.panelX) : (float) this.panelX);
        if (uoK) {
            this.contentView.setAlpha(Math.max(0.1f, this.decelerateInterpolator.getInterpolation(this.mPanelPositionRatio)));
        }
        if (this.uoH != null) {
            this.uoH.overlayScrollChanged(this.mPanelPositionRatio);
        }
    }

    final void fv(int i) {
        cnF();
        this.mSettling = true;
        this.slidingPanelLayoutInterpolator.dt(getMeasuredWidth(), i);
    }

    final void closePanel(int i) {
        if (DEBUG) {
            Log.d("wo.SlidingPanelLayout", "onPanelClosing");
        }
        this.mIsPageMoving = true;
        if (this.uoH != null) {
            boolean z;
            t tVar = this.uoH;
            if (this.mTouchState == 1) {
                z = true;
            } else {
                z = false;
            }
            tVar.oc(z);
        }
        this.mSettling = true;
        this.slidingPanelLayoutInterpolator.dt(0, i);
    }

    public final void em(View view) {
        if (this.uoB != null) {
            super.removeView(this.uoB);
        }
        this.uoB = view;
        super.addView(this.uoB, 0);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        acquireVelocityTrackerAndAddMovement(motionEvent);
        if (getChildCount() <= 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mTouchState == 1) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                boolean z;
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (DEBUG) {
                    String valueOf = String.valueOf(motionEvent);
                    Log.d("wo.SlidingPanelLayout", "Intercept touch down: " + valueOf);
                }
                this.mDownX = x;
                this.mDownY = y;
                this.uoF = (float) this.panelX;
                this.mLastMotionX = x;
                this.mTotalMotionX = 0.0f;
                this.mActivePointerId = motionEvent.getPointerId(0);
                action = Math.abs(this.slidingPanelLayoutInterpolator.mFinalX - this.panelX);
                if (this.slidingPanelLayoutInterpolator.isFinished() || action < this.mTouchSlop / 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z || this.mForceDrag) {
                    this.mForceDrag = false;
                    cnN();
                    this.uoE = x;
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetTouchState();
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.mActivePointerId != -1) {
                    determineScrollingStart(motionEvent, 1.0f);
                    break;
                }
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                releaseVelocityTracker();
                break;
        }
        if (this.mTouchState == 0) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (this.contentView == null) {
            return super.onTouchEvent(motionEvent);
        }
        acquireVelocityTrackerAndAddMovement(motionEvent);
        float x;
        float y;
        int abs;
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                boolean z;
                x = motionEvent.getX();
                y = motionEvent.getY();
                this.mDownX = x;
                this.mDownY = y;
                this.uoF = (float) this.panelX;
                this.mLastMotionX = x;
                this.mTotalMotionX = 0.0f;
                this.mActivePointerId = motionEvent.getPointerId(0);
                abs = Math.abs(this.slidingPanelLayoutInterpolator.mFinalX - this.panelX);
                if (this.slidingPanelLayoutInterpolator.isFinished() || abs < this.mTouchSlop / 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (z && !this.mForceDrag) {
                    return true;
                }
                this.mForceDrag = false;
                cnN();
                this.uoE = x;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (this.mTouchState != 1) {
                    return true;
                }
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                abs = (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                boolean z2 = this.mTotalMotionX > 25.0f && Math.abs(abs) > this.mFlingThresholdVelocity;
                if (z2) {
                    if (this.mIsRtl) {
                        abs = -abs;
                    }
                    if (Math.abs(abs) < this.mMinFlingVelocity) {
                        if (abs >= 0) {
                            fv(750);
                        } else {//Todo: this else was not there initially
                            closePanel(750);
                        }
                    } else {
                        float measuredWidth = ((float) (getMeasuredWidth() / 2)) + (((float) Math.sin((double) ((float) (((double) (Math.min(1.0f, (((float) (abs < 0 ? this.panelX : getMeasuredWidth() - this.panelX)) * 1.0f) / ((float) getMeasuredWidth())) - 0.5f)) * 0.4712389167638204d)))) * ((float) (getMeasuredWidth() / 2)));
                        if (abs > 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        abs = Math.round(Math.abs(measuredWidth / ((float) Math.max(this.mMinSnapVelocity, Math.abs(abs)))) * 1000.0f) * 4;
                        if (z2) {
                            fv(abs);
                        } else {
                            closePanel(abs);
                        }
                    }
                } else {
                    if (this.panelX >= getMeasuredWidth() / 2) {
                        fv(750);
                    } else {//Todo: this else was not there initially
                        closePanel(750);
                    }
                }
                resetTouchState();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (this.mTouchState == 1) {
                    abs = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (abs == -1) {
                        return true;
                    }
                    y = motionEvent.getX(abs);
                    this.mTotalMotionX += Math.abs(y - this.mLastMotionX);
                    this.mLastMotionX = y;
                    y -= this.uoE;
                    x = this.uoF;
                    if (this.mIsRtl) {
                        y = -y;
                    }
                    setPanelX((int) (y + x));
                    return true;
                }
                determineScrollingStart(motionEvent, 1.0f);
                return true;
            case MotionEvent.ACTION_POINTER_UP://6:
                onSecondaryPointerUp(motionEvent);
                releaseVelocityTracker();
                return true;
            default:
                return true;
        }
    }

    private final void resetTouchState() {
        releaseVelocityTracker();
        this.mForceDrag = false;
        this.mTouchState = 0;
        this.mActivePointerId = -1;
    }

    private final void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() >> 8) & 255;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            action = action == 0 ? 1 : 0;
            float x = motionEvent.getX(action);
            this.uoE += x - this.mLastMotionX;
            this.mDownX = x;
            this.mLastMotionX = x;
            this.mActivePointerId = motionEvent.getPointerId(action);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    protected void determineScrollingStart(MotionEvent motionEvent, float f) {
        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        if (findPointerIndex != -1) {
            float x = motionEvent.getX(findPointerIndex);
            if (((int) Math.abs(x - this.mDownX)) > Math.round(((float) this.mTouchSlop) * f)) {
                this.mTotalMotionX += Math.abs(this.mLastMotionX - x);
                this.uoE = x;
                this.mLastMotionX = x;
                cnN();
            }
        }
    }

    private final void acquireVelocityTrackerAndAddMovement(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            this.mVelocityTracker.clear();
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private final void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    protected void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (this.uoB != null) {
            this.uoB.measure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size2, MeasureSpec.EXACTLY));//Todo: i modified them, there was ints before instead of constants
        }
        if (this.contentView != null) {
            this.contentView.measure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size2, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(size, size2);
        setPanelX((int) (((float) size) * this.mPanelPositionRatio));
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.uoB != null) {
            this.uoB.layout(0, 0, this.uoB.getMeasuredWidth(), this.uoB.getMeasuredHeight());
        }
        if (this.contentView != null) {
            int measuredWidth = this.contentView.getMeasuredWidth();
            int measuredHeight = this.contentView.getMeasuredHeight();
            int i5 = this.mIsRtl ? measuredWidth : -measuredWidth;
            if (this.mIsRtl) {
                measuredWidth *= 2;
            } else {
                measuredWidth = 0;
            }
            this.contentView.layout(i5, 0, measuredWidth, measuredHeight);
        }
    }

    public static boolean isRtl(Resources resources) {
        if (VERSION.SDK_INT < 17 || resources.getConfiguration().getLayoutDirection() != 1) {
            return false;
        }
        return true;
    }

    private final void cnN() {
        this.mTouchState = 1;
        this.mIsPageMoving = true;
        this.mSettling = false;
        this.slidingPanelLayoutInterpolator.cnP();
        if (uoL) {
            setLayerType(2, null);
        }
        if (DEBUG) {
            Log.d("wo.SlidingPanelLayout", "onDragStarted");
        }
        if (this.uoH != null) {
            this.uoH.drag();
        }
    }

    final void cnF() {
        if (DEBUG) {
            Log.d("wo.SlidingPanelLayout", "onPanelOpening");
        }
        this.mIsPageMoving = true;
        if (this.uoH != null) {
            this.uoH.cnF();
        }
    }

    final void cnG() {
        if (DEBUG) {
            Log.d("wo.SlidingPanelLayout", "onPanelOpened");
        }
        cnO();
        this.mIsPanelOpen = true;
        this.mIsPageMoving = false;
        if (this.uoH != null) {
            this.uoH.open();
        }
    }

    final void cnO() {
        if (uoL) {
            setLayerType(0, null);
        }
    }
}
