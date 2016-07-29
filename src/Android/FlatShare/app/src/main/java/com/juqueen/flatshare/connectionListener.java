package com.juqueen.flatshare;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by juqueen on 4/10/2016.
 */
public class connectionListener implements WifiP2pManager.ConnectionInfoListener {

    public static Context context;
    private static WifiP2pInfo info;

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {


        this.info = info;
      /* Toast.makeText(context, "GO ="
                        + ((info.isGroupOwner == true) ? "Yes"
                      : "no"), Toast.LENGTH_SHORT);
*/
               Log.e("conlis", "GO ="
                       + ((info.isGroupOwner == true) ? "Yes"
                       : "no"));
      // Toast.makeText(context, ("Group Owner IP - " + info.groupOwnerAddress.getHostAddress()), Toast.LENGTH_SHORT);
        Log.e("conlis",info.groupOwnerAddress.getHostAddress());
    }

    public static connectionListener getInstance() {
        return new connectionListener() ;
    }

    public static WifiP2pInfo infoReturn(Context ctx)
    {
        return info;
    }
}
