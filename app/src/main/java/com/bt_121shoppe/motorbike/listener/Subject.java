package com.bt_121shoppe.motorbike.listener;

import com.bt_121shoppe.motorbike.receiver.NetworkBroadcastReceiver;

public interface Subject {
    void registerObserver(OnNetworkConnectionChangeListener listener);

    void unregisterObserver(OnNetworkConnectionChangeListener listener);

    void notifyNetworkObserverChange(NetworkBroadcastReceiver.NetworkState networkState);
}
