package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by administrator on 16/1/17.
 */

public class ListDownloadOrderDetails {

    private int status;
    private ArrayList<DownloadOrderDetailsModel> order_details = new ArrayList<>();

    public ArrayList<DownloadOrderDetailsModel> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<DownloadOrderDetailsModel> order_details) {
        this.order_details = order_details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
