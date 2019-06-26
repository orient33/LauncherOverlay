package com.google.android.apps.gsa.nowoverlayservice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.google.android.libraries.gsa.d.a.OverlayController;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.fabianterhorst.server.LogTool;
import io.fabianterhorst.server.MyRecyclerViewAdapter;
import io.fabianterhorst.server.R;

/*
06-27 10:48:30.990 27862 27862 D drawer.server: Overlay. new()!
06-27 10:48:30.995 27862 27862 D drawer.server: Overlay.onCreate(Bundle)
06-27 10:48:31.033 27862 27862 D drawer.server: OverlayController.setTouchable. false
06-27 10:48:31.034 27862 27862 D drawer.server: OverlayController.onActivityStateFlag. 3
06-27 10:48:31.034 27862 27862 D drawer.server: OverlayController. onStart
06-27 10:48:31.034 27862 27862 D drawer.server: OverlayController. onResume             //一旦client onResume.那么server也进入resume
06-27 10:48:36.258 27862 27862 D drawer.server: OverlayController.setTouchable. true      //client滑动显示server时
06-27 10:48:37.467 27862 27862 D drawer.server: OverlayController.setTouchable. false     //滑回client时
06-27 10:48:38.992 27862 27862 D drawer.server: OverlayController.onActivityStateFlag. 0//client destroy时
06-27 10:48:38.992 27862 27862 D drawer.server: OverlayController. onPause
06-27 10:48:38.992 27862 27862 D drawer.server: OverlayController. onStop
06-27 10:48:39.508 27862 27862 D drawer.server: OverlayController.onActivityStateFlag. 0
06-27 10:48:39.509 27862 27862 D drawer.server: OverlayController. onDestroy
 */
public class Overlay extends OverlayController { // extends DialogOverlayController  extends ContextThemeWrapper

    private final Context context;

    public Overlay(Context context, int appTheme, int dialogTheme) {
        super(context, R.style.AppTheme, R.style.Theme_SearchNow);
        this.context = context;
        LogTool.log("Overlay. new()!");
    }

    public final void onCreate(Bundle bundle) {
        LogTool.log("Overlay.onCreate(Bundle)");
        container.setFitsSystemWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View rowView = LayoutInflater.from(context).inflate(R.layout.activity_second, this.container, true);

        //通过findViewById拿到RecyclerView实例
        RecyclerView recyclerView = rowView.findViewById(R.id.recyclerView);
        //设置RecyclerView管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //初始化适配器
        List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            arrayList.add("卡片: " + (i + 1));
        }
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(arrayList);
        //设置添加或删除item时的动画，这里使用默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        recyclerView.setAdapter(mAdapter);
    }
}
