package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khrishi on 1/1/17.
 */
public class MedicinePojo implements Parcelable {

    private int med_id;
    private String med_name;

    public String getPharm_id() {
        return pharm_id;
    }

    public void setPharm_id(String pharm_id) {
        this.pharm_id = pharm_id;
    }

    private String pharm_id;

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    private String pharm_name;
    private double med_price;

    public int getMed_id() {
        return med_id;
    }

    public void setMed_id(int med_id) {
        this.med_id = med_id;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public double getMed_price() {
        return med_price;
    }

    public void setMed_price(double med_price) {
        this.med_price = med_price;
    }

    public MedicinePojo() {}

    protected MedicinePojo(Parcel in) {

        med_id = in.readInt();
        med_name = in.readString();
        med_price = in.readDouble();

    }

    public static final Creator<MedicinePojo> CREATOR = new Creator<MedicinePojo>() {
        @Override
        public MedicinePojo createFromParcel(Parcel in) {
            return new MedicinePojo(in);
        }

        @Override
        public MedicinePojo[] newArray(int size) {
            return new MedicinePojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(med_id);
        dest.writeString(med_name);
        dest.writeDouble(med_price);
    }
}
