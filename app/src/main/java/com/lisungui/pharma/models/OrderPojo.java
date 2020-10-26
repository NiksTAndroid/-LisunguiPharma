package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khrishi on 7/1/17.
 */
public class OrderPojo implements Parcelable {

    private int order_id;
    private int order_server_id;
    private int order_med_id;
    private String order_med_name;
    private double order_price;
    private int order_qty;
    private double order_total_price;
    private String order_date;
    private String order_return_date;
    private String order_delivery_date;
    private String order_status;

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

    public int getOrder_med_id() {
        return order_med_id;
    }

    public void setOrder_med_id(int order_med_id) {
        this.order_med_id = order_med_id;
    }

    public String getOrder_med_name() {
        return order_med_name;
    }

    public void setOrder_med_name(String order_med_name) {
        this.order_med_name = order_med_name;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public int getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(int order_qty) {
        this.order_qty = order_qty;
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

    public String getOrder_return_date() {
        return order_return_date;
    }

    public void setOrder_return_date(String order_return_date) {
        this.order_return_date = order_return_date;
    }

    public String getOrder_delivery_date() {
        return order_delivery_date;
    }

    public void setOrder_delivery_date(String order_delivery_date) {
        this.order_delivery_date = order_delivery_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public OrderPojo() {}

    protected OrderPojo(Parcel in) {

        order_id = in.readInt();
        order_server_id = in.readInt();
        order_med_id = in.readInt();
        order_med_name = in.readString();
        order_price = in.readDouble();
        order_qty = in.readInt();
        order_total_price = in.readDouble();
        order_date = in.readString();
        order_return_date = in.readString();
        order_delivery_date = in.readString();
        order_status = in.readString();
    }

    public static final Creator<OrderPojo> CREATOR = new Creator<OrderPojo>() {
        @Override
        public OrderPojo createFromParcel(Parcel in) {
            return new OrderPojo(in);
        }

        @Override
        public OrderPojo[] newArray(int size) {
            return new OrderPojo[size];
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
        dest.writeInt(order_med_id);
        dest.writeString(order_med_name);
        dest.writeDouble(order_price);
        dest.writeInt(order_qty);
        dest.writeDouble(order_total_price);
        dest.writeString(order_date);
        dest.writeString(order_return_date);
        dest.writeString(order_delivery_date);
        dest.writeString(order_status);

    }
}
