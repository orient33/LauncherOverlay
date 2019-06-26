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
import io.fabianterhorst.server.MyRecyclerViewAdapter;
import io.fabianterhorst.server.R;

public class Overlay extends OverlayController {

    private final Context context;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    public Overlay(Context context, int i, int i2) {
        super(context, R.style.AppTheme, R.style.Theme_SearchNow);
        this.context = context;
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.container.setFitsSystemWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View rowView = LayoutInflater.from(context).inflate(R.layout.activity_second, this.container, true);

        //通过findViewById拿到RecyclerView实例
        mRecyclerView = rowView.findViewById(R.id.recyclerView);
//设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//初始化适配器
        List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            arrayList.add("卡片" + (i + 1));
        }
        mAdapter = new MyRecyclerViewAdapter(arrayList);
//设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }
}
