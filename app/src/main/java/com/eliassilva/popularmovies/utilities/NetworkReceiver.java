package com.eliassilva.popularmovies.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Elias on 01/03/2018.
 */

/**
 * Manage the internet connection
 */
public class NetworkReceiver extends BroadcastReceiver {
    private boolean wasTrueFlag;
    private NetworkReceiverListener networkReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            wasTrueFlag = true;
        }
        networkReceiverListener.onConnectionChange(wasTrueFlag);
    }

    /**
     * Determines if there is a connection to the internet
     *
     * @param context of the activity
     * @return true if there is a connection and false if not
     */
    private boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conn != null;
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        return (networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE));
    }

    public interface NetworkReceiverListener {
        void onConnectionChange(boolean wasTrueFlag);
    }

    public void setNetworkReceiverListener(NetworkReceiverListener networkReceiverListener) {
        this.networkReceiverListener = networkReceiverListener;
    }
}
