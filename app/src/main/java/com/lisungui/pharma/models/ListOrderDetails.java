package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by khrishi on 15/1/17.
 */
public class ListOrderDetails {

    private int status;
    private ArrayList<OrderDetails> order_details = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<OrderDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<OrderDetails> order_details) {
        this.order_details = order_details;
    }
}
