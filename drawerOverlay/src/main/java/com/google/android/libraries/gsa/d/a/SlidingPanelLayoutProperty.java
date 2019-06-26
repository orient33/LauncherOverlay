package com.google.android.libraries.gsa.d.a;

import android.util.Property;

final class SlidingPanelLayoutProperty extends Property<SlidingPanelLayout, Integer> {

    SlidingPanelLayoutProperty(Class<Integer> cls, String str) {
        super(cls, str);
    }

    public final /* synthetic */ Integer get(SlidingPanelLayout view) {
        return view.panelX;
    }

    public final /* synthetic */ void set(SlidingPanelLayout view, Integer value) {
        view.setPanelX(value);
    }
}
