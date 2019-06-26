package com.google.android.libraries.launcherclient;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.a.a;

public final class ILauncherOverlayCallback_Stub_Proxy extends a implements ILauncherOverlayCallback {

    ILauncherOverlayCallback_Stub_Proxy(IBinder iBinder) {
        super(iBinder, "com.google.android.libraries.launcherclient.ILauncherOverlayCallback");
    }

    @Override
    public final void scrollChanged(float progress) {
        Parcel pg = getParcelWithIT();
        pg.writeFloat(progress);
        reply(1, pg);
    }

    @Override
    public final void statusChanged(int status) {
        Parcel pg = getParcelWithIT();
        pg.writeInt(status);
        reply(2, pg);
    }
}
