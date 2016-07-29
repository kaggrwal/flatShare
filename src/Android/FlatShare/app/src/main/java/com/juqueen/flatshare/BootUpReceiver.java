package com.juqueen.flatshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by juqueen on 4/2/2016.
 */
public class BootUpReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mintent = new Intent(context,WifiDirect.class);
        context.startService(mintent);
        Log.e("boorcv", "caurcv");

        mintent = new Intent(context,globalServerService.class);
        context.startService(mintent);

        mintent = new Intent(context,loadingScreen.class);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        //context.startActivity(mintent);
        //Intent myIntent = new Intent(context, WifiDirect.class);
        //context.startService(myIntent);
    }
}
