package com.juqueen.flatshare;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by juqueen on 7/25/2016.
 */
public class PacketTranslator {

    PacketProto.Packet packetRcvd;
    InputStream inputStream;
    OutputStream outputStream;
    boolean flagMembership = true;
    PacketProto.Packet packetResp;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    int returnCode;


    public PacketTranslator(PacketProto.Packet packetRcvd, InputStream iStream, OutputStream oStream)
    {

        this.packetRcvd = packetRcvd;
        this.inputStream = iStream;
        this.outputStream = oStream;
        translate();

    }

    private void translate() {

        switch(packetRcvd.getHeader(0).getCode())
        {

            case(MessageCodes.JOIN_REQ):
                joinReqRecvd();
                break;
            case(MessageCodes.JOIN_APPROVE):
                joinApproveRcvd();
                break;
            case(MessageCodes.FLAT_DB_REQ):
                flatDbReqRecvd();

        }
    }

    private void flatDbReqRecvd() {

        packetResp = PacketProto.Packet.newBuilder().addHeader(PacketProto.Packet.Header.newBuilder().setCode(MessageCodes.DB_FILE_RESP).setData("N").setDataLength(0).build()).addData(PacketProto.Packet.Data.newBuilder().setUniquePcktId(1234)).build();
    }

    private void joinApproveRcvd() {

        //dir crea
        flatList.messageCode = MessageCodes.FLAT_DB_REQ;
    }

    private void joinReqRecvd() {

        // chk membership

        if(flagMembership)
        {
            setReturnCode(MessageCodes.JOIN_APPROVE);
        }
        else
        {
            setReturnCode(MessageCodes.JOIN_DISCARD);
        }

    }





}
