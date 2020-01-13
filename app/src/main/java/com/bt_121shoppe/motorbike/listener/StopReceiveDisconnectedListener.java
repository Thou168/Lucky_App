package com.bt_121shoppe.motorbike.listener;

import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;

public abstract class StopReceiveDisconnectedListener implements OnNetworkConnectionChangeListener {
    private boolean mIsReadyReceiveConnectedListener;

    public boolean isReadyReceiveConnectedListener() {
        return mIsReadyReceiveConnectedListener;
    }

    @Deprecated
    public boolean stopReceiveDisconnectedListener() {
        return true;
    }

    @Override
    public void onConnected() {
        mIsReadyReceiveConnectedListener = true;
        onNetworkConnected();
    }

    public void onNetworkConnected() {

    }
}
