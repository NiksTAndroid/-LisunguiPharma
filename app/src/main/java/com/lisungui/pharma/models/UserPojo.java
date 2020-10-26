package com.lisungui.pharma.models;

/**
 * Created by administrator on 23/1/17.
 */

public class UserPojo {
    private int user_id;
    private int user_server_id;
    private String user_name;
    private String user_email_id;
    private String user_gender;
    private String user_mb_no;
    private String user_address;

    public String getUser_insurnace() {
        return user_insurnace;
    }

    public void setUser_insurnace(String user_insurnace) {
        this.user_insurnace = user_insurnace;
    }

    private String user_insurnace;

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public int getUser_server_id() {
        return user_server_id;
    }

    public void setUser_server_id(int user_server_id) {
        this.user_server_id = user_server_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public void setUser_email_id(String user_email_id) {
        this.user_email_id = user_email_id;
    }

    public String getUser_mb_no() {
        return user_mb_no;
    }

    public void setUser_mb_no(String user_mb_no) {
        this.user_mb_no = user_mb_no;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }
}
