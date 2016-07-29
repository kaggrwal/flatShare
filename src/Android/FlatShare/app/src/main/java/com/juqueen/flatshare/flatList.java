package com.juqueen.flatshare;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.juqueen.flatshare.MessageCodes.*;

public class flatList extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton createBtn;
    private FloatingActionButton joinBtn;
    private TextView emptyFlats;
    private globalFlatDbManager manager;
    private List<flatData> flatDatas;
    private ArrayList<String> itemName;
    private Button clin;

    private WifiDirect wifiDirectService;
    private boolean mBound;
    List peers;
    private WifiP2pDevice GO;
    private ProgressDialog progressDialog;
    private WifiP2pInfo info;

    ListView list;

    public static int messageCode = JOIN_REQ;


    /*String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };*/

    Integer[] imgid={
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_list);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menuFLatList);
        createBtn = (FloatingActionButton) findViewById(R.id.fabFlatToCreate);
        joinBtn = (FloatingActionButton) findViewById(R.id.fabFlatToJoin);
        emptyFlats = (TextView) findViewById(R.id.emptyFlat);
        clin = (Button) findViewById(R.id.buttoncln);
        clin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                info  =     connectionListener.infoReturn(flatList.this);
                //Socket socket = new Socket();

              new FileServerAsyncTask(flatList.this,info).execute();





            }
        });


        /**
         * A simple server socket that accepts connection and writes some data on
         * the stream.
         */





        createBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);

        manager = new globalFlatDbManager(this);

        flatDatas = manager.getAllRows();


        if(flatDatas.size() != 0)
        {

            emptyFlats.setVisibility(View.INVISIBLE);
            flatRetrieval();
            Intent mIntent = new Intent(flatList.this,globalServerService.class);
            startService(mIntent);
        }
        else{


            emptyFlats.setVisibility(View.VISIBLE);
            emptyFlats.setText("Currently there are no flats, Create One");
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, WifiDirect.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WifiDirect.LocalBinder binder = (WifiDirect.LocalBinder) service;
            wifiDirectService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void flatRetrieval(){


        itemName = new ArrayList<String>();


        for(flatData flatData : flatDatas)
        {
            String nickName = flatData.getNickName();
            if(null != nickName && !nickName.isEmpty())
            {
                itemName.add(flatData.getName()+"("+nickName+")");
            }
            else
            {
                itemName.add(flatData.getName());
            }


        }

        customFlatListAdapter adapter=new customFlatListAdapter(this, itemName, imgid);
        list=(ListView)findViewById(R.id.flatList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                flatData selectedflat = flatDatas.get(position);
                Toast.makeText(getApplicationContext(), selectedflat.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(flatList.this,flatActivity.class);
                //Bundle mBundle = new Bundle();
                //mBundle.putParcelable("selectedflat", selectedflat);
                intent.putExtra("selectedflat",selectedflat);
                startActivity(intent);



            }
        });

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();


        if(id == R.id.fabFlatToCreate)
        {
            Intent intent = new Intent(flatList.this, flatProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
        else if(id == R.id.fabFlatToJoin)
        {

            connectionListener.context = flatList.this;
            peers = wifiDirectService.getPeers();
            GO = (WifiP2pDevice)peers.get(goMatch(peers));
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = GO.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = ProgressDialog.show(this, "Press back to cancel",
                    "Connecting to :" + GO.deviceAddress, true, true
//
            );

            wifiDirectService.connect(config);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }





        }



    }

    private int goMatch(List peers) {

        return 0;

    }




    public static class FileServerAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        Socket socket = new Socket();
        private WifiP2pInfo info;
        // private TextView statusText;


        public  FileServerAsyncTask(Context context, WifiP2pInfo info) {
            this.context = context;
            this.info = info;

            // this.statusText = (TextView) statusText;
        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                socket.bind(null);
                socket.connect((new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), 8988)), 5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("clin", "Client socket - " + socket.isConnected());


            while(true) {
                PacketProto.Packet PacketReq;
                PacketProto.Packet PacketRcvd;

                PacketReq = new PacketFiller(messageCode).getPacket();
                Log.e("clin","msgcode   "+messageCode);
                Log.e("clin","PacketReq   "+PacketReq.toString());
                try {
                    PacketReq.writeDelimitedTo(socket.getOutputStream());
                    PacketRcvd = PacketProto.Packet.parseDelimitedFrom(socket.getInputStream());
                    Log.e("clin","PacketRcvd   "+PacketRcvd.toString());
                    new PacketTranslator(PacketRcvd,socket.getInputStream(),socket.getOutputStream());


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }






            /*FileStructureProto.File filePacket;




            File fileIp =new File("/storage/sdcard1/abc.mp4");

            List<ByteString> fileParts = new ArrayList<ByteString>();

            fileChunker chunker = new fileChunker(fileIp);

            boolean flag = chunker.requiredChunking();

            //fileParts = chunker.partition();

            int no_parts = chunker.noChunks();
            Log.e("clin",  "par "+no_parts);

            AddressBookProtos.Person pck = AddressBookProtos.Person.newBuilder().setName("File").build();
            try {
                pck.writeDelimitedTo(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int i=1;i<=no_parts;i++)
            {
                filePacket = FileStructureProto.File.newBuilder().setName(fileIp.getName())
                        .setSrc("/storage/sdcard1/").setDes("/storage/sdcard1/")
                        .setNoChunks(no_parts).setSeqNo(i).setType(FileStructureProto.File.FileType.OTHER)
                        .setData(ByteString.copyFrom(chunker.toByteArray())).build();
                chunker.setStart(chunker.getStart()+(int)chunker.getCHUNK_SIZE());
                Log.e("clin", "seq_no is " + i);
                try {
                    filePacket.writeDelimitedTo(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filePacket = null;
               *//* try {
                    socket.getOutputStream().flush();
                    finalize();9-
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }*//*

            }*/

            /*try {
                socket.getOutputStream().flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
*/


            //Log.e("clin", pck.toString());
            //DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            //OutputStream stream = socket.getOutputStream();
            //stream.writeUTF("Hello admin");
            //pck.writeDelimitedTo(socket.getOutputStream());



        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {


        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            //statusText.setText("Opening a server socket");
        }


    }



}




