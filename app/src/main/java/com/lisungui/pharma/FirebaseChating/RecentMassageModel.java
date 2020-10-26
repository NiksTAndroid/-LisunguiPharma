package com.lisungui.pharma.FirebaseChating;

public class RecentMassageModel {



    public RecentMassageModel(String date, String user1Name, String user2Name, String user1ID, String user2ID, String massage_file) {
        this.date = date;
        this.user1Name = user1Name;
        this.user2Name = user2Name;
        this.user1ID = user1ID;
        this.user2ID = user2ID;
        this.massage_file = massage_file;
    }

    public RecentMassageModel() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public String getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(String user1ID) {
        this.user1ID = user1ID;
    }

    public String getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(String user2ID) {
        this.user2ID = user2ID;
    }

    public String getMassage_file() {
        return massage_file;
    }

    public void setMassage_file(String massage_file) {
        this.massage_file = massage_file;
    }



    private String date;
    private String key;
    private String user1Name;
    private String user2Name;
    private String user1ID;
    private String user2ID;
    private String massage_file;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
