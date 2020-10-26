package com.lisungui.pharma.FirebaseChating;

public class ChattingMassageModel {




    public ChattingMassageModel(String msgID, String date,String senderType, String senderID, String senderName, String receiverID, String receiverType, boolean isRead, int type, String massage_file) {
        this.msgID = msgID;
        this.date = date;
        this.senderID = senderID;
        this.senderType = senderType;
        this.senderName = senderName;
        this.receiverID = receiverID;
        this.receiverType = receiverType;
        this.isRead = isRead;
        this.type = type;
        this.massage_file = massage_file;
    }

    private String msgID;
    private String senderType;
    private String date;
    private String senderID;

    public ChattingMassageModel() {
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }

    public Double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(Double location_long) {
        this.location_long = location_long;
    }

    public String getMassage_file() {
        return massage_file;
    }

    public void setMassage_file(String massage_file) {
        this.massage_file = massage_file;
    }

    private String senderName;
    private String receiverID;
    private String receiverType;
    private boolean isRead;
    private int type;
    private Double location_lat;


    private Double location_long;
    private String massage_file;


    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
}
