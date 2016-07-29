package com.juqueen.flatshare;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by juqueen on 4/8/2016.
 */
public class globalServerService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

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

            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8988);
                Log.e("globalServer", "Server: Socket opened");
                Socket server = serverSocket.accept();


                while (true) {

                    PacketProto.Packet PacketRcvd = PacketProto.Packet.parseDelimitedFrom(server.getInputStream());
                    Log.e("globalServer", "Packetrcvd   "+PacketRcvd.toString());
                    if(PacketRcvd.getHeader(0).getCode() == MessageCodes.JOIN_REQ)
                    {
                        PacketProto.Packet PacketResp = new PacketFiller(new PacketTranslator(PacketRcvd,server.getInputStream(),server.getOutputStream()).getReturnCode()).getPacket();
                        PacketResp.writeDelimitedTo(server.getOutputStream());
                        Log.e("globalServer", "Packetresp  " + PacketResp.toString());


                    }

                    //DataInputStream is = new DataInputStream(client.getInputStream());
                    //InputStream is = client.getInputStream();
                /*while (true) {
                    //Log.e("globalServer", "in while");

                    //AddressBookProtos.Person rcv = AddressBookProtos.Person.parseDelimitedFrom(client.getInputStream());


                    //File file = new File(getApplicationContext().getFilesDir()+rcv.getName());
                    //File file = new File(Environment.getExternalStorageDirectory()+"/abc.mp4");


                    //chunker.initiateWriting();


                    while (true) {


                        AddressBookProtos.Person rcv = AddressBookProtos.Person.parseDelimitedFrom(client.getInputStream());
                        Log.e("globalServer", rcv.getName());
                        if (rcv.getName().equals("File")) {

                            long startTime = System.currentTimeMillis();
                            long stopTime = System.currentTimeMillis();

                            FileStructureProto.File filePacket = null;
                            int n = 1, i = 1;
                            File fileOp = new File("/storage/sdcard1/abc.mp4");
                            fileChunker chunker = new fileChunker(fileOp);
                            FileOutputStream outputStream = new FileOutputStream(fileOp);
                            while (true) {
                                filePacket = FileStructureProto.File.parseDelimitedFrom(client.getInputStream());
                                Log.e("globalServer", filePacket.getData().toString());
                                n = filePacket.getNoChunks();
                                if (!(i == filePacket.getSeqNo())) break;
                                outputStream.write(filePacket.getData().toByteArray());
                                //chunker.reconstitute(filePacket.getData().toByteArray());
                                Log.e("globalServer", "seq rcv " + filePacket.getSeqNo() + " loop no " + i + " no chunks " + filePacket.getNoChunks());
                                if (i == n) {
                                    outputStream.flush();
                                    outputStream.close();
                                    break;
                                    //chunker.closeWriting();
                                    //finalize();
                                }
                                i++;
                            }
                            long elapsedTime = stopTime - startTime;
                            Log.e("globalServer", filePacket.getName() + "recvd in " + elapsedTime);
                        }
                    }

                    // outputStream.flush();
                    //outputStream.close();


                    //chunker.reconstitute();


                    //Log.e("globalServer", rcv.toString());
                    //rcvd = is.readUTF();
                    // Toast.makeText(globalServerService.this, "globalServer   " + filePacket.getName(), Toast.LENGTH_SHORT).show();
                }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }


        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        Log.e("globalServer", "inside read1");

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Log.e("globalServer", "inside read2..." + line);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.


        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "globalServerService starting", Toast.LENGTH_SHORT).show();

        Log.e("globalServer", "globalServerService starting");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        Log.e("globalServer", "service done");
    }
}
