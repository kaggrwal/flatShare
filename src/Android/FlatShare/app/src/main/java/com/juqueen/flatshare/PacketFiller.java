package com.juqueen.flatshare;

import java.util.Random;

/**
 * Created by juqueen on 7/24/2016.
 */
public class PacketFiller {

    int Message_Code = 0;
    PacketProto.Packet Packet;
    private static int ID = 0;

    public PacketFiller(int Code) {
        this.Message_Code = Code;
        fillPacket();
        //Log.e("clin", String.valueOf(Code+"  "+Message_Code));
    }

    private void fillPacket() {
        switch (Message_Code) {

            case MessageCodes.JOIN_REQ:
                fillJoinRequest();
                break;

            case MessageCodes.JOIN_APPROVE:
                fillJoinApprove();
                break;

            case MessageCodes.JOIN_DISCARD:
                fillJoinDiscard();
                break;

            case MessageCodes.INFO_MEMBERS_REQ:
                fillInfoMembersReq();
                break;

        }


    }

    private void fillInfoMembersReq() {
        Id_generator();
        Packet = PacketProto.Packet.newBuilder()
                .addHeader(PacketProto.Packet.Header.newBuilder().setCode(MessageCodes.INFO_MEMBERS_REQ).setData("Y").setDataLength(32).build())
                .addData(PacketProto.Packet.Data.newBuilder().setJoineeId(0).setUniquePcktId(ID).build()).build();
    }

    private void fillJoinApprove() {

        Id_generator();
        Packet = PacketProto.Packet.newBuilder()
                .addHeader(PacketProto.Packet.Header.newBuilder().setCode(MessageCodes.JOIN_APPROVE).setData("Y").setDataLength(32).build())
                .addData(PacketProto.Packet.Data.newBuilder().setJoineeId(0).setUniquePcktId(ID).build()).build();
    }

    private void fillJoinDiscard() {

        Id_generator();
        Packet = PacketProto.Packet.newBuilder()
                .addHeader(PacketProto.Packet.Header.newBuilder().setCode(MessageCodes.JOIN_DISCARD).setData("Y").setDataLength(32).build())
                .addData(PacketProto.Packet.Data.newBuilder().setJoineeId(0).setUniquePcktId(ID).build()).build();
    }

    private void Id_generator() {
        Random num = new Random();
        ID = num.nextInt(10000);
    }

    private void fillJoinRequest() {

        Id_generator();
        Packet = PacketProto.Packet.newBuilder()
                .addHeader(PacketProto.Packet.Header.newBuilder().setCode(MessageCodes.JOIN_REQ).setData("Y").setDataLength(32).build())
                .addData(PacketProto.Packet.Data.newBuilder().setJoineeId(1234).setUniquePcktId(ID).build()).build();


    }



    public PacketProto.Packet getPacket()
    {
        return Packet;
    }


}
