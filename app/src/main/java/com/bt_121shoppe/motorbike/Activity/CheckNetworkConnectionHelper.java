package com.bt_121shoppe.motorbike.Activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.bt_121shoppe.motorbike.listener.OnNetworkConnectionChangeListener;
import com.bt_121shoppe.motorbike.listener.StopReceiveDisconnectedListener;
import com.bt_121shoppe.motorbike.listener.SubscribeNetworkObserverChange;
import com.bt_121shoppe.motorbike.receiver.NetworkBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;


public class CheckNetworkConnectionHelper extends SubscribeNetworkObserverChange
        implements LifecycleObserver {
    private static final String TAG = CheckNetworkConnectionHelper.class.getName();
    private static CheckNetworkConnectionHelper sNetworkConnection;

    private AppCompatActivity mAppCompatActivity;
    private Context mCurrentContext;
    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;

    private NetworkBroadcastReceiver.NetworkState mNetworkState = NetworkBroadcastReceiver.NetworkState.NOTHING;

    public static CheckNetworkConnectionHelper getInstance() {
        if (sNetworkConnection == null) {
            sNetworkConnection = new CheckNetworkConnectionHelper();
        }
        return sNetworkConnection;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        removeStateChangeListener();
    }

    public void registerNetworkChangeListener(OnNetworkConnectionChangeListener listener) {
        if (listener.getContext() == null) {
            return;
        }
        mCurrentContext = listener.getContext();
        AppCompatActivity appCompatActivity = (AppCompatActivity) mCurrentContext;
        if (mAppCompatActivity == null) {
            mAppCompatActivity = appCompatActivity;
        }
        appCompatActivity.getLifecycle().addObserver(this);
        checkAddNetworkChangeListener(listener);
        registerObserver(listener);

        if (mNetworkBroadcastReceiver == null) {
            mNetworkBroadcastReceiver = new NetworkBroadcastReceiver(networkState -> {
                mNetworkState = networkState;
                List<OnNetworkConnectionChangeListener> lastListener = getLastNetworkChangeListener();
                checkAddNetworkChangeListener(lastListener);
            });
            mAppCompatActivity.registerReceiver(mNetworkBroadcastReceiver,
                    mNetworkBroadcastReceiver.getIntentFilter());
        }
    }

    @Deprecated
    public void onNetworkConnectionChange(Activity activity,
                                          OnNetworkConnectionChangeListener onNetworkConnectionChangeListener) {
        if (activity == null) {
            return;
        }
        mCurrentContext = activity;
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        if (mAppCompatActivity == null) {
            mAppCompatActivity = appCompatActivity;
        }
        appCompatActivity.getLifecycle().addObserver(this);
        checkAddNetworkChangeListener(onNetworkConnectionChangeListener);
        registerNetworkChangeListener(onNetworkConnectionChangeListener);

        if (mNetworkBroadcastReceiver == null) {
            mNetworkBroadcastReceiver = new NetworkBroadcastReceiver(networkState -> {
                mNetworkState = networkState;
                List<OnNetworkConnectionChangeListener> listener = getLastNetworkChangeListener();
                checkAddNetworkChangeListener(listener);
            });
            mAppCompatActivity.registerReceiver(mNetworkBroadcastReceiver,
                    mNetworkBroadcastReceiver.getIntentFilter());
        }
    }

    private static void removeInstance() {
        sNetworkConnection = null;
    }

    private void removeStateChangeListener() {
        if (mAppCompatActivity == null
                || getNetworkObserverList().isEmpty()) {
            return;
        }

        List<OnNetworkConnectionChangeListener> listeners = getLastNetworkChangeListener();
        Log.e(TAG, "remove: " + listeners.size());
        for (OnNetworkConnectionChangeListener listener : listeners) {
            unregisterObserver(listener);
        }

        if (getNetworkObserverList().isEmpty()
                && mNetworkBroadcastReceiver != null) {
            //mAppCompatActivity.unregisterReceiver(mNetworkBroadcastReceiver);
            mCurrentContext = null;
            mNetworkBroadcastReceiver = null;
            removeInstance();

        } else {
            int lastIndex = getNetworkObserverList().size() - 1;
            mCurrentContext = getNetworkObserverList().get(lastIndex).getContext();

            List<OnNetworkConnectionChangeListener> lastNetworkChangeListener = getLastNetworkChangeListener();
            checkAddNetworkChangeListener(lastNetworkChangeListener);

        }
    }

    private void checkAddNetworkChangeListener(List<OnNetworkConnectionChangeListener> listeners) {
        for (OnNetworkConnectionChangeListener listener : listeners) {
            checkAddNetworkChangeListener(listener);
        }
    }

    private void checkAddNetworkChangeListener(OnNetworkConnectionChangeListener listener) {
        if (listener != null && !(listener instanceof StopReceiveDisconnectedListener
                && ((StopReceiveDisconnectedListener) listener).isReadyReceiveConnectedListener())) {
            if (mNetworkState == NetworkBroadcastReceiver.NetworkState.CONNECTED) {
                listener.onConnected();
            } else if (mNetworkState == NetworkBroadcastReceiver.NetworkState.DISCONNECTED) {
                listener.onDisconnected();
            }
        }
    }

    private List<OnNetworkConnectionChangeListener> getLastNetworkChangeListener() {
        List<OnNetworkConnectionChangeListener> dataList = new ArrayList<>();
        for (OnNetworkConnectionChangeListener listener : getNetworkObserverList()) {
            Log.e(TAG, "My Context: " + listener.getContext()
                    + " == " + mCurrentContext);
            if (listener.getContext() != null
                    && listener.getContext().equals(mCurrentContext)) {
                dataList.add(listener);
            }
        }
        return dataList;
    }
}
