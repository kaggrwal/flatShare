package com.juqueen.flatshare;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Harmony on 12/17/2015.
 */
public class xmlProfile {


    private String email;
    private String dob;
    private String fullName;
    private String contactNumber;
    private String MAC;
    private Context ctx;
    private XmlPullParserFactory parserFactory;
    public volatile boolean parsingComplete = true;






    public xmlProfile(String _fullName, String _email, String _dob, String _contactNumber, Context _ctx) {

        this.fullName = _fullName;
        this.email = _email;
        this.contactNumber = _contactNumber;
        this.dob = _dob;
        this.ctx = _ctx;


        if(_fullName != null)
        {
            WifiManager wifiMan = (WifiManager) ctx.getSystemService(
                    ctx.WIFI_SERVICE);
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            MAC = wifiInf.getMacAddress();
            write_to_disk(ctx);
        }

    }

    private void write_to_disk(Context ctx) {


        File dataDirectory = directoryCheck(ctx);
        File aboutXml = new File(dataDirectory,"about.xml");

            FileOutputStream ostream = null;
            try {
                //if(checkExternalMedia()) {
                    //aboutXml.createNewFile();
                    ostream = new FileOutputStream(aboutXml);
                //}
                xmlWriter(ostream);

            } catch (IOException e) {
                e.printStackTrace();

            }


    }



    public String getFullName() {
        return fullName;
    }


    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }


    public String getMAC() {
        return MAC;
    }

    public void parseXMLAndStoreIt() throws XmlPullParserException, FileNotFoundException {

        parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser myParser = parserFactory.newPullParser();
        File dataDirectory = directoryCheck(ctx);
        File aboutXml = new File(dataDirectory,"about.xml");
        FileInputStream stream = new FileInputStream(aboutXml);
        myParser.setInput(stream,null);

        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;


                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;


                    case XmlPullParser.END_TAG:
                        if(name.equals("FullName")){
                            fullName = text;
                        }

                        else if(name.equals("Email")){
                            email = text;
                        }

                        else if(name.equals("DOB")){
                            dob = text;
                        }

                        else if(name.equals("ContactNo")){
                            contactNumber = text;
                        }
                        else if(name.equals("MAC")){
                            MAC = text;
                        }

                        else{
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void xmlWriter(FileOutputStream ostream) {

        int i = 1;
        XmlSerializer serializer = Xml.newSerializer();

        try {
            serializer.setOutput(ostream, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "info");

            switch (i) {
                case 1:
                    serializer.startTag(null, "FullName");

                    serializer.text(fullName.toString());

                    serializer.endTag(null, "FullName");
                    i++;

                case 2:
                    serializer.startTag(null, "Email");

                    serializer.text(email.toString());

                    serializer.endTag(null, "Email");
                    i++;

                case 3:
                    serializer.startTag(null, "DOB");

                    serializer.text(dob.toString());

                    serializer.endTag(null, "DOB");
                    i++;

                case 4:

                    String temp;
                    serializer.startTag(null, "ContactNo");

                    //temp = contactNumber.substring(contactNumber.length() / 2, 0) + contactNumber.substring((contactNumber.length() / 2) + 1, contactNumber.length() - 1);
                    serializer.text(contactNumber.toString());

                    serializer.endTag(null, "ContactNo");
                    i++;

                case 5:

                    serializer.startTag(null, "MAC");

                    serializer.text(MAC.toString());

                    serializer.endTag(null, "MAC");
                    i++;
                    break;


            }

            serializer.endTag(null, "info");
            serializer.endDocument();

            serializer.flush();

            ostream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private File directoryCheck(Context ctx) {

        File dataDirectory = new File(ctx.getFilesDir()+"/flatshare/data/profile/info");
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                Log.e("ProfCre ", "Problem creating profile file");
                return null;
            }
        }
        return dataDirectory;


    }





}





