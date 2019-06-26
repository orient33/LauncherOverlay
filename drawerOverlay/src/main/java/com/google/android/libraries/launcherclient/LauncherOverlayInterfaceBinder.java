package com.google.android.libraries.launcherclient;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.WindowManager.LayoutParams;

import com.google.android.a.LauncherOverlayBinder;
import com.google.android.a.ParcelUtil;
//import com.google.android.gms.dynamite.descriptors.com.google.android.gms.ads.dynamite.ModuleDescriptor;

public abstract class LauncherOverlayInterfaceBinder extends LauncherOverlayBinder implements a {

    protected LauncherOverlayInterfaceBinder() {
        attachInterface(this, "com.google.android.libraries.launcherclient.ILauncherOverlay");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {//Todo: throws is new
        ILauncherOverlayCallback callback = null;
        if (a(i, parcel, parcel2, i2)) {
            return true;
        }
        IBinder readStrongBinder;
        IInterface _callback;
        boolean isRunning;
        //1..x 意义见 ILauncherOverlay.java
        switch (i) {
            case 1://startScroll
                startScroll();
                break;
            case 2://onScroll(float)
                onScroll(parcel.readFloat());
                break;
            case 3://endScroll()
                endScroll();
                break;
            case 4://windowAttached(...)
                LayoutParams layoutParams = (LayoutParams) ParcelUtil.readLayoutParams(parcel, LayoutParams.CREATOR);
                readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder != null) {
                    _callback = readStrongBinder.queryLocalInterface("com.google.android.libraries.launcherclient.ILauncherOverlayCallback");
                    if (_callback instanceof ILauncherOverlayCallback) {
                        callback = (ILauncherOverlayCallback) _callback;
                    } else {
                        callback = new ILauncherOverlayCallback_Stub_Proxy(readStrongBinder);
                    }
                }
                windowAttached(layoutParams, callback, parcel.readInt());
                break;
            case 5://windowDetached(...)
                windowDetached(ParcelUtil.a(parcel));
                break;
            case 6://closeOverlay(int)
                closeOverlay(parcel.readInt());
                break;
            case 7://onPause()
                onPause();
                break;
            case 8:
                onResume();
                break;
            case 9://openOverlay(int)
                openOverlay(parcel.readInt());
                break;
            case 10://requestVoiceDetection(boolean)
                requestVoiceDetection(ParcelUtil.a(parcel));
                break;
            case 11://getVoiceSearchLanguage
                String HB = getVoiceSearchLanguage();
                parcel2.writeNoException();
                parcel2.writeString(HB);
                break;
            case /*ModuleDescriptor.MODULE_VERSION*/ 12 /*12*/://Todo: modified, 12 was always there but the constant was there before
                //isVoiceDetectionRunning
                isRunning = isVoiceDetectionRunning();
                parcel2.writeNoException();
                ParcelUtil.a(parcel2, isRunning);
                break;
            case 13:
                isRunning = cnM();//always true
                parcel2.writeNoException();
                ParcelUtil.a(parcel2, isRunning);
                break;
            case 14:
                Bundle bundle = (Bundle) ParcelUtil.readLayoutParams(parcel, Bundle.CREATOR);
                readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder != null) {
                    _callback = readStrongBinder.queryLocalInterface("com.google.android.libraries.launcherclient.ILauncherOverlayCallback");
                    if (_callback instanceof ILauncherOverlayCallback) {
                        callback = (ILauncherOverlayCallback) _callback;
                    } else {
                        callback = new ILauncherOverlayCallback_Stub_Proxy(readStrongBinder);
                    }
                }
                windowAttached(bundle, callback);
                break;
            case 16:
                onLifeState(parcel.readInt());
                break;
            case 17:
                isRunning = isVoiceDetectionRunning(parcel.createByteArray(), (Bundle) ParcelUtil.readLayoutParams(parcel, Bundle.CREATOR));
                parcel2.writeNoException();
                ParcelUtil.a(parcel2, isRunning);
                break;
            default:
                return false;
        }
        return true;
    }
}
