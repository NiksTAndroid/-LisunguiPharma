package com.lisungui.pharma.models;

/**
 * Created by siddeshwar on 11/1/17.
 */
public class PlaceOrderPojo {

    private int status;
    private int order_id;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
