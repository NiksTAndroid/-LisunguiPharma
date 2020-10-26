package com.lisungui.pharma.models;

public class Notification {

    private int not_id;
    private int not_order_id;
    private String not_msg;
    private String not_date;

    public int getNot_id() {
        return not_id;
    }

    public void setNot_id(int not_id) {
        this.not_id = not_id;
    }

    public int getNot_order_id() {
        return not_order_id;
    }

    public void setNot_order_id(int not_order_id) {
        this.not_order_id = not_order_id;
    }

    public String getNot_msg() {
        return not_msg;
    }

    public void setNot_msg(String not_msg) {
        this.not_msg = not_msg;
    }

    public String getNot_date() {
        return not_date;
    }

    public void setNot_date(String not_date) {
        this.not_date = not_date;
    }
}
