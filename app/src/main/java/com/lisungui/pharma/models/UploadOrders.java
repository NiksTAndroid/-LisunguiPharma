package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by siddeshwar on 11/1/17.
 */
public class UploadOrders {


    ArrayList<MedDetails> order_details = new ArrayList<>();

    public ArrayList<MedDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<MedDetails> order_details) {
        this.order_details = order_details;
    }
}
