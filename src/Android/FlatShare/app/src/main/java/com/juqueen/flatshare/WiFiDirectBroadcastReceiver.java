package com.juqueen.flatshare;

/**
 * Created by juqueen on 4/1/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/***
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiDirect mActivity;
    private WifiP2pManager.PeerListListener peerListListener;
    private IntentFilter mIntentFilter;
    private static boolean isWfdEnabled;
    public static String res;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WifiDirect activity,WifiP2pManager.PeerListListener peerListListener) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.peerListListener = peerListListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            handleWifiP2pStateChanged(intent);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            handleWifiP2pPeersChanged(intent);
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            handleWifiP2pConnectionChanged(intent);

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            handleWifiP2pThisDeviceChanged(intent);
        }



    }

    private void handleWifiP2pConnectionChanged(Intent intent) {

        if (mManager == null) {
            return;
        }

        NetworkInfo networkInfo = (NetworkInfo) intent
                .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

        if (networkInfo.isConnected()) {

            // we are connected with the other device, request connection
            // info to find group owner IP


            mManager.requestConnectionInfo(mChannel,connectionListener.getInstance());
        } else {
            // It's a disconnect
            //activity.resetData();
        }

    }

    private void restartDiscovery(Context context) {

        Intent intent = new Intent(context,WifiDirect.class);
        context.startService(intent);



    }

    private void handleWifiP2pPeersChanged(Intent intent) {

        if (mManager != null) {
            mManager.requestPeers(mChannel, peerListListener);
        }
        Log.d("Wifidirservice", "P2P peers changed");

    }

    private void handleWifiP2pThisDeviceChanged(Intent intent) {

        WifiP2pDevice thisDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);

        Log.i("Wifidirservice", "Device " + thisDevice.deviceAddress + " " + thisDevice.deviceName);
        res = "Device "+thisDevice.deviceAddress+" "+thisDevice.deviceName;

    }

    private void handleWifiP2pStateChanged(Intent intent) {



        int wfdState = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
        isWfdEnabled = wfdState == WifiP2pManager.WIFI_P2P_STATE_ENABLED ? true : false;
        Log.e("Wifidirservice", isWfdEnabled == true ? "wfdEnabled" : "wfdNotEnabled");

        res = isWfdEnabled == true ?"wfdEnabled" : "wfdNotEnabled";




    }

    public void registerReciever() {

       mActivity.registerReceiver(this,getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        if(mIntentFilter == null)
        {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        }



        return mIntentFilter;
    }


    public void unregisterReciever() {

        mActivity.unregisterReceiver(this);
//

    }
}
