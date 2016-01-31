package com.juqueen.flatshare;

/**
 * Created by juqueen on 1/31/2016.
 */
public class flatData {


    private String name;
    private String nickName;
    private String address;
    private String flatId;
    private String ownerName;
    private String coverPicPath;

    public flatData(String name,String nickName,String address,String flatId, String ownerName, String coverPicPath)
    {
        this.name = name;
        this.nickName = nickName;
        this.address  = address;
        this.flatId = flatId;
        this.ownerName = ownerName;
        this.coverPicPath = coverPicPath;

        id_generator();

    }

    private void id_generator() {

        int nameLength = 0;

        if(nickName != null && !nickName.isEmpty())
        {
            nameLength = nickName.length();
        }



    }
}
