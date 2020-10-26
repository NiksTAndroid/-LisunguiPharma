package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MedDetails implements Parcelable {

    private int detail_id;
    private int detail_order_id;
    private int detail_fk_order_id;
    private int detail_med_id;
    private String med_name;
    private double detail_med_price;
    //private double med_price;
    private int detail_med_qnty;
    //private int med_qnty;
    private String detail_qnty;
    private double med_price;
    public String getDetail_qnty() {
        return detail_qnty;
    }

    public void setDetail_qnty(String detail_qnty) {
        this.detail_qnty = detail_qnty;
    }

    public double getmed_price() {
        return med_price;
    }

    public void setmed_price(double med_price) {
        this.med_price = med_price;
    }



    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public int getDetail_fk_order_id() {
        return detail_fk_order_id;
    }

    public void setDetail_fk_order_id(int detail_fk_order_id) {
        this.detail_fk_order_id = detail_fk_order_id;
    }

    public int getDetail_order_id() {
        return detail_order_id;
    }

    public void setDetail_order_id(int detail_order_id) {
        this.detail_order_id = detail_order_id;
    }

    public int getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(int detail_id) {
        this.detail_id = detail_id;
    }

    public int getDetail_med_id() {
        return detail_med_id;
    }

    public void setDetail_med_id(int detail_med_id) {
        this.detail_med_id = detail_med_id;
    }

    public double getDetail_med_price() {
        return detail_med_price;
    }

    public void setDetail_med_price(double detail_med_price) {
        this.detail_med_price = detail_med_price;
    }

    public int getDetail_med_qnty() {
        return detail_med_qnty;
    }

    public void setDetail_med_qnty(int detail_med_qnty) {
        this.detail_med_qnty = detail_med_qnty;
    }

    public MedDetails() {}

    protected MedDetails(Parcel in) {

        detail_id = in.readInt();
        detail_order_id = in.readInt();
        detail_fk_order_id = in.readInt();
        detail_med_id = in.readInt();
        med_name = in.readString();
        detail_med_price = in.readDouble();
        detail_med_qnty = in.readInt();

        detail_qnty = in.readString();
    }

    public static final Creator<MedDetails> CREATOR = new Creator<MedDetails>() {
        @Override
        public MedDetails createFromParcel(Parcel in) {
            return new MedDetails(in);
        }

        @Override
        public MedDetails[] newArray(int size) {
            return new MedDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(detail_id);
        dest.writeInt(detail_order_id);
        dest.writeInt(detail_fk_order_id);
        dest.writeInt(detail_med_id);
        dest.writeString(med_name);
        dest.writeDouble(detail_med_price);
        dest.writeInt(detail_med_qnty);
        dest.writeString(detail_qnty);
    }
}
