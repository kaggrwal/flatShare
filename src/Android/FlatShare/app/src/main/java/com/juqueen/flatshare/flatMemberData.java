package com.juqueen.flatshare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juqueen on 7/26/2016.
 */
public class flatMemberData {

    private String memberName;
    private String memberDOB;

    public String getMemberDesignation() {
        return memberDesignation;
    }

    public void setMemberDesignation(String memberDesignation) {
        this.memberDesignation = memberDesignation;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberDOB() {
        return memberDOB;
    }

    public void setMemberDOB(String memberDOB) {
        this.memberDOB = memberDOB;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    private String memberDesignation;
    private String flatId;
    private String memberID;

    public flatMemberData(String memberName, String memberDOB, String memberDesignation, String flatId, String memberID) {
        if (memberID != null || !memberID.isEmpty()) {
            this.memberName = memberName;
            this.memberDesignation = memberDesignation;
            this.memberDOB = memberDOB;
            this.memberID = memberID;
            this.flatId = flatId;


        }


    }

    public flatMemberData() {
        //do nothing
    }

    protected flatMemberData(Parcel in) {
        memberName = in.readString();
        memberID = in.readString();
        memberDOB = in.readString();
        flatId = in.readString();
        memberDesignation = in.readString();

    }

    public static final Parcelable.Creator<flatMemberData> CREATOR = new Parcelable.Creator<flatMemberData>() {
        @Override
        public flatMemberData createFromParcel(Parcel source) {
            flatMemberData flatMemberData = new flatMemberData();
            flatMemberData.memberDesignation = source.readString();
            flatMemberData.memberDOB = source.readString();
            flatMemberData.memberID = source.readString();
            flatMemberData.memberName = source.readString();
            flatMemberData.flatId = source.readString();

            return flatMemberData;
        }

        @Override
        public flatMemberData[] newArray(int size) {
            return new flatMemberData[size];
        }
    };


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(memberName);
        dest.writeString(memberDesignation);
        dest.writeString(memberDOB);
        dest.writeString(flatId);
        dest.writeString(memberID);

    }
}
