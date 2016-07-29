package com.juqueen.flatshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juqueen on 3/20/2016.
 */
public class WifiDirect extends Service implements WifiP2pManager.ChannelListener {


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private WiFiDirectBroadcastReceiver mReciever;
    private List peers;
    private WifiP2pManager.PeerListListener peerListListener;

    private IBinder binder = new LocalBinder();
    ProgressDialog progressDialog = null;


    public class LocalBinder extends Binder {
        WifiDirect getService() {
            // Return this instance of LocalService so clients can call public methods
            return WifiDirect.this;
        }
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        Looper rcvdLooper;
        public ServiceHandler(Looper looper) {
            super(looper);
            rcvdLooper = looper;
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
 /*           try {
                Log.e("serviceimp",msg.toString());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }




 */           // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);

            mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
            mChannel = mManager.initialize(WifiDirect.this,rcvdLooper,WifiDirect.this);
            peers =  new ArrayList();
            peerListListener = new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

                    peers.clear();
                    peers.addAll(wifiP2pDeviceList.getDeviceList());

                    // If an AdapterView is backed by this data, notify it
                    // of the change.  For instance, if you have a ListView of available
                    // peers, trigger an update.
                    //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
                    if (peers.size() == 0) {
                        Log.d("Wifidirservice", "No devices found");
                        return;
                    }
                    else
                    {
                        Log.d("Wifidirservice", "Found "+ peers.get(0).toString());
                        displayToast("Found "+ peers.get(0).toString());
                    }



                }
            };

            mReciever = new WiFiDirectBroadcastReceiver(mManager,mChannel,WifiDirect.this,peerListListener);

            register_mReciever();

            if(mReciever != null)
            {
                discoverPeers();
            }
        }
    }


    public List getPeers(){
        return peers;
    }

    public void onInitiateDiscovery(Activity activity) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(activity, "Press back to cancel", "finding peers", true,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

        discoverPeers();
    }

    public void connect(WifiP2pConfig config) {
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WifiDirect.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.


        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Log.e("Wifidirservice","service starting");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void discoverPeers() {


        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                displayToast("ini discover Peers");
                Log.e("Wifidirservice","ini discover Peers");
            }



            @Override
            public void onFailure(int i) {

            }
        });
    }

    private void unregister_mReciever() {

        if(mReciever != null)
        {
            mReciever.unregisterReciever();
        }

        mReciever = null;


    }

    private void register_mReciever() {


        mReciever.registerReciever();

        //Log.e("Wifidirservice", WiFiDirectBroadcastReceiver.res);
        displayToast(WiFiDirectBroadcastReceiver.res);

    }




    @Override
    public void onChannelDisconnected() {
        displayToast("WiFi Channel disconnected: Reinitializing...");

        Log.e("Wifidirservice", "WiFi Channel disconnected: Reinitializing...");
        reIntializeChannel();

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        Log.e("Wifidirservice", "service done");
    }

    private void reIntializeChannel() {

        mChannel = mManager.initialize(this,getMainLooper(),this);

        if(mChannel != null)
        {
            displayToast("Wifi Channel reinitialization: SUCCESS");
            Log.e("Wifidirservice","Wifi Channel reinitialization: SUCCESS");

        }

        else
        {
            displayToast("Wifi Channel reinitialization: SUCCESS");
        }


    }

    private void displayToast(String s) {

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}


