package com.androideradev.www.moviespots;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ConnectivityStatus {

    private static final String TAG = "ConnectivityStatus";
    private static volatile ConnectivityStatus sConnectivityStatus;
    private final MutableLiveData<NetworkStatus> mLiveData;

    private ConnectivityStatus(Context context) {
        mLiveData = new MutableLiveData<>(NetworkStatus.LOST);
        initializeConnectivityManger(context);
    }

    public static ConnectivityStatus getConnectivityStatus(Context context) {
        if (sConnectivityStatus == null) {
            synchronized (ConnectivityStatus.class) {
                if (sConnectivityStatus == null) {
                    sConnectivityStatus = new ConnectivityStatus(context);
                }
            }
        }
        return sConnectivityStatus;
    }

    public LiveData<NetworkStatus> getNetworkStatus() {
        return mLiveData;
    }

    private void initializeConnectivityManger(Context context) {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager connectivityManager =
                ContextCompat.getSystemService(context, ConnectivityManager.class);
        if (connectivityManager != null) {
            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    Log.d(TAG, "onAvailable: ");
                    mLiveData.postValue(NetworkStatus.AVAILABLE);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Log.d(TAG, "onLost: ");
                    mLiveData.postValue(NetworkStatus.LOST);
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
                }

            };

            connectivityManager.requestNetwork(networkRequest, networkCallback);
        }
    }

    public enum NetworkStatus {
        AVAILABLE("Network Available!"), LOST("Network Not Available!");

        private final String mMessage;

        NetworkStatus(String message) {
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }


    }

}
