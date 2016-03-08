package com.juqueen.flatshare;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by juqueen on 1/31/2016.
 */
public class flatData implements Parcelable {


    private String name;
    private String nickName;
    private String address;
    private String flatId;
    private String ownerName;
    private String coverPicPath;

    public flatData(String name, String nickName, String address, String ownerName, String coverPicPath) {
        if (name != null || !name.isEmpty()) {
            this.name = name;
            this.nickName = nickName;
            this.address = address;
            this.ownerName = ownerName;
            this.coverPicPath = coverPicPath;

            id_generator();
        }


    }

    public flatData() {
        //do nothing
    }

    protected flatData(Parcel in) {
        name = in.readString();
        nickName = in.readString();
        address = in.readString();
        flatId = in.readString();
        ownerName = in.readString();
        coverPicPath = in.readString();
    }

    public static final Creator<flatData> CREATOR = new Creator<flatData>() {
        @Override
        public flatData createFromParcel(Parcel source) {
            flatData flatData = new flatData();
            flatData.name = source.readString();
            flatData.nickName = source.readString();
            flatData.flatId = source.readString();
            flatData.address = source.readString();
            flatData.ownerName = source.readString();
            return flatData;
        }

        @Override
        public flatData[] newArray(int size) {
            return new flatData[size];
        }
    };

    private void id_generator() {

        Random random = new Random();

        String name = getName();
        if (name != null || !name.isEmpty()) {
            flatId = name.charAt(0) + String.valueOf(random.nextInt(999999)) + name.charAt(name.length() - 1);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(nickName);
        dest.writeString(flatId);
        dest.writeString(address);
        dest.writeString(ownerName);

    }


}