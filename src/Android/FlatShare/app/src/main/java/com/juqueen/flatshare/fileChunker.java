package com.juqueen.flatshare;

import com.google.protobuf.ByteString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by juqueen on 5/4/2016.
 */
public class fileChunker {

    private File fileInput = null;
    //private File fileOutput = null;
    private long fileInputLength = 0;
    private List<ByteString> fileParts = null;
    private FileOutputStream bufferedOutputStream = null;

    public long getCHUNK_SIZE() {
        return CHUNK_SIZE;
    }

    private long CHUNK_SIZE = 4*1024*1024;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    private int start =0;


    public fileChunker(File file)
    {
        this.fileInput = file;
    }



    public boolean requiredChunking()
    {

        this.fileInputLength = fileInput.length();

        if(fileInputLength > CHUNK_SIZE)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public int noChunks()
    {
        int no_parts = (int)(fileInputLength/CHUNK_SIZE)+1;
        return no_parts;
    }

   /* public List<ByteString> partition()  {

        fileParts = new ArrayList<ByteString>();


        int no_parts = noChunks();
        System.out.println(no_parts);
        long off = CHUNK_SIZE;
        int start=0;
        int count = (int)off;
        for(int i =1;i<=no_parts;i++)
        {
            //System.out.println(start);

            //AddressBookProtos.Person.File filePart = AddressBookProtos.Person.File.newBuilder().setSiz(ByteString.copyFrom(toByteArray(start, count))).build();
            //fileParts.add(ByteString.copyFrom(toByteArray(start, count)));
            start = start+(int) off;

        }



        return fileParts;


    }
*/
    public byte[] toByteArray() {

        int count = Math.min((int)CHUNK_SIZE, (int)fileInputLength-this.getStart());
        BufferedInputStream inputStream = null;
        byte[] array = new byte[count];
        try {
            inputStream = new BufferedInputStream(new FileInputStream(fileInput));
            inputStream.skip(this.getStart());
            for (int currentByte = 0; currentByte < count; currentByte++)
            {
                // load one byte from the input file and write it to the output file
                array[currentByte]= (byte) inputStream.read();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return array;
    }


    public void initiateWriting()
    {
        try {
            bufferedOutputStream = new FileOutputStream(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public  void reconstitute(byte[] bPart) {
        // TODO Auto-generated method stub
      //  int size=fileParts.size();
        //System.out.println(fileParts.get(0));

        //FileOutputStream outputStream = null;
        try {

          bufferedOutputStream.write(bPart);
           // bufferedOutputStream.flush();

            //outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void closeWriting()
    {
        try {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
