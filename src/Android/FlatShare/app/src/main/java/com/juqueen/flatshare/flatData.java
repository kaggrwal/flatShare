package com.juqueen.flatshare;

import java.util.Random;

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

    public flatData(String name,String nickName,String address, String ownerName, String coverPicPath)
    {
        if(name!= null || !name.isEmpty())
        {
            this.name = name;
            this.nickName = nickName;
            this.address = address;
            this.ownerName = ownerName;
            this.coverPicPath = coverPicPath;

            id_generator();
        }

        
    }

    public flatData(){
        //do nothing
    }

    private void id_generator() {

        Random random = new Random();

            String name = getName();
        if(name != null || !name.isEmpty())
        {
            flatId = name.charAt(0)+String.valueOf(random.nextInt(999999))+name.charAt(name.length()-1);
        }


    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCoverPicPath() {
        return coverPicPath;
    }

    public void setCoverPicPath(String coverPicPath) {
        this.coverPicPath = coverPicPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
