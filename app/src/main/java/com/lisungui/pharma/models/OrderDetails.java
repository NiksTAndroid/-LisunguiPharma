package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderDetails implements Parcelable {

    private int order_id;
    private int order_server_id;
    private int order_qnty;
    private double order_total_price;
    private String order_date;
    private String order_track_status;
    private String order_update_date;

    private ArrayList<MedDetails> order_data = new ArrayList<>();

    public ArrayList<MedDetails> getOrder_data() {
        return order_data;
    }

    public void setOrder_data(ArrayList<MedDetails> order_data) {
        this.order_data = order_data;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_server_id() {
        return order_server_id;
    }

    public void setOrder_server_id(int order_server_id) {
        this.order_server_id = order_server_id;
    }

    public int getOrder_qnty() {
        return order_qnty;
    }

    public void setOrder_qnty(int order_qnty) {
        this.order_qnty = order_qnty;
    }

    public double getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(double order_total_price) {
        this.order_total_price = order_total_price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_track_status() {
        return order_track_status;
    }

    public void setOrder_track_status(String order_track_status) {
        this.order_track_status = order_track_status;
    }

    public String getOrder_update_date() {
        return order_update_date;
    }

    public void setOrder_update_date(String order_update_date) {
        this.order_update_date = order_update_date;
    }

    public OrderDetails() {}

    protected OrderDetails(Parcel in) {

        order_id = in.readInt();
        order_server_id = in.readInt();
        order_qnty = in.readInt();
        order_total_price = in.readDouble();
        order_date = in.readString();
        order_track_status = in.readString();
        order_update_date = in.readString();
        in.readTypedList(order_data, MedDetails.CREATOR);
    }

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(order_id);
        dest.writeInt(order_server_id);
        dest.writeInt(order_qnty);
        dest.writeDouble(order_total_price);
        dest.writeString(order_date);
        dest.writeString(order_track_status);
        dest.writeString(order_update_date);
        dest.writeTypedList(order_data);
    }
}