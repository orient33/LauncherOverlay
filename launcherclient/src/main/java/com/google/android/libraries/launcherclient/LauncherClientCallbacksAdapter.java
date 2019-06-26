package com.google.android.libraries.launcherclient;

public class LauncherClientCallbacksAdapter implements LauncherClientCallbacks {
    private WorkspaceOverlayCallbacks mWorkspace;

    public LauncherClientCallbacksAdapter(WorkspaceOverlayCallbacks workspace) {
        mWorkspace = workspace;
    }

    @Override
    public void onOverlayScrollChanged(float progress) {
        mWorkspace.onWorkspaceOverlayScrollChanged(progress);
    }

    @Override
    public void onServiceStateChanged(boolean overlayAttached, boolean hotwordActive) {

    }
}
