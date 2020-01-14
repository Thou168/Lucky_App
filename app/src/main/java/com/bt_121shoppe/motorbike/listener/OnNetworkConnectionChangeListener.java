package com.bt_121shoppe.motorbike.listener;

import android.content.Context;

public interface OnNetworkConnectionChangeListener {
    void onConnected();

    void onDisconnected();

    Context getContext();
}
