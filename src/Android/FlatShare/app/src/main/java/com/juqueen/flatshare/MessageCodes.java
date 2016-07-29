package com.juqueen.flatshare;

/**
 * Created by juqueen on 7/24/2016.
 */
public class MessageCodes {


    //Global Codes

    public static final int PROFILE_REQ= 0x3EA; //1002

    public static final int PROFILE_RESP = 0x3EB; //1003

    public static final int DB_FILE_REQ= 0x3EC; //1004

    public static final int DB_FILE_RESP = 0x3ED; //1005

    public static final int ACK = 0x3E8; //1000

    public static final int FLAT_DB_REQ = 0x3EE; //1006

    public static final int FLAT_DB_RESP = 0x3EF; //1007





    /// JOIN CODES series 100



    //Requests related JOIN

    public static final int JOIN_REQ = 0x66; //102





    //Info related JOIN

    public static final int INFO_MEMBERS_REQ = 0x67; //103

    public static final int INFO_MEMBERS_RESP = 0x68; //104



    //Response related JOIN

    public static final int JOIN_APPROVE = 0x6A; //106

    public static final int JOIN_DISCARD = 0x6C; //108




}
