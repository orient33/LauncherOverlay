package io.fabianterhorst.launcheroverlay;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.libraries.launcherclient.LauncherClient;
import com.google.android.libraries.launcherclient.LauncherClientCallbacksAdapter;
import com.google.android.libraries.launcherclient.WorkspaceOverlayCallbacks;

public class HomeActivity extends AppCompatActivity implements WorkspaceOverlayCallbacks {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private LauncherClient launcherClient;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CallbacksAdapter callbacksAdapter = new CallbacksAdapter(this);
        launcherClient = new LauncherClient(this, callbacksAdapter,
                "io.fabianterhorst.server", true);
//        launcherClient = new LauncherClient(this, callbacksAdapter,  true);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    launcherClient.updateMove(1f - positionOffset);
                }
                Log.d(TAG, "onPageScrolled:" + position + ";Offset:" + positionOffset + ";Pixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged:position" + state);
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        launcherClient.endMove();
                        viewPager.setCurrentItem(1);
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                    case ViewPager.SCROLL_STATE_SETTLING:
                        launcherClient.startMove();
                        break;
                }
            }
        });
    }

    public void showAnimated(View sender) {
        viewPager.setCurrentItem(0, true);
    }

    /* Below is various event forwarding to Google Now */

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (launcherClient != null) {
            launcherClient.onAttachedToWindow();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (launcherClient != null) {
            launcherClient.onDetachedFromWindow();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (launcherClient != null) {
            launcherClient.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (launcherClient != null) {
            launcherClient.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (launcherClient != null) {
            launcherClient.onDestroy();
        }
    }

    @Override
    public void onWorkspaceOverlayScrollChanged(float v) {

    }

    class CallbacksAdapter extends LauncherClientCallbacksAdapter {
        CallbacksAdapter(WorkspaceOverlayCallbacks workspace) {
            super(workspace);
        }

        @Override
        public void onOverlayScrollChanged(float progress) {
            //Log.d("CallbacksAdapter", "onOverlayScrollChanged: \n  progress: " + progress);
        }

        @Override
        public void onServiceStateChanged(boolean overlayAttached, boolean hotwordActive) {
            //Log.d("CallbacksAdapter", "onServiceStateChanged: \n  overlayAttached: " + overlayAttached + "\n  hotwordActive: " + hotwordActive);
        }
    }
}