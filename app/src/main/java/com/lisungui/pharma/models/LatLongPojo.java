package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by siddeshwar on 15/12/16.
 */
public class LatLongPojo implements Serializable {
  /*  @SerializedName("pharm_id")
    @Expose
    private String pharm_id;
    private String pharm_name;
    private double pharm_lat;
    private double pharm_lng;
    private String pharm_mb_no;
    private double pharm_distance;
    private String pharm_address;
    private String pharm_night;
    private String pharm_s_day;
    private String pharm_e_day;
    private String pharm_s_time;
    private String pharm_e_time;

    private String pharm_insurance;//pharm_insurance

    public String getPharm_insurance() {
        return pharm_insurance;
    }

    public void setPharm_insurance(String pharm_insurance) {
        this.pharm_insurance = pharm_insurance;
    }

*//*
    private String pharm_24;

    public String getPharm_24() {
        return pharm_24;
    }

    public void setPharm_24(String pharm_24) {
        this.pharm_24 = pharm_24;
    }
*//*

    public String getPharm_s_day() {
        return pharm_s_day;
    }

    public void setPharm_s_day(String pharm_s_day) {
        this.pharm_s_day = pharm_s_day;
    }

    public String getPharm_e_day() {
        return pharm_e_day;
    }

    public void setPharm_e_day(String pharm_e_day) {
        this.pharm_e_day = pharm_e_day;
    }

    public String getPharm_s_time() {
        return pharm_s_time;
    }

    public void setPharm_s_time(String pharm_s_time) {
        this.pharm_s_time = pharm_s_time;
    }

    public String getPharm_e_time() {
        return pharm_e_time;
    }

    public void setPharm_e_time(String pharm_e_time) {
        this.pharm_e_time = pharm_e_time;
    }

    public String getPharm_night() {
        return pharm_night;
    }

    public void setPharm_night(String pharm_night) {
        this.pharm_night = pharm_night;
    }

    public double getPharm_distance() {
        return pharm_distance;
    }

    public void setPharm_distance(double pharm_distance) {
        this.pharm_distance = pharm_distance;
    }

    public String getPharm_mb_no() {
        return pharm_mb_no;
    }

    public void setPharm_mb_no(String pharm_mb_no) {
        this.pharm_mb_no = pharm_mb_no;
    }

    public String getPharm_address() {
        return pharm_address;
    }

    public void setPharm_address(String pharm_address) {
        this.pharm_address = pharm_address;
    }

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public double getPharm_lat() {
        return pharm_lat;
    }

    public void setPharm_lat(double pharm_lat) {
        this.pharm_lat = pharm_lat;
    }

    public double getPharm_lng() {
        return pharm_lng;
    }

    public void setPharm_lng(double pharm_lng) {
        this.pharm_lng = pharm_lng;
    }

    public LatLongPojo() {}

    protected LatLongPojo(Parcel in) {

        pharm_name = in.readString();
        pharm_id = in.readString();
        pharm_lat = in.readDouble();
        pharm_lng = in.readDouble();
        pharm_mb_no = in.readString();
        pharm_distance = in.readDouble();
        pharm_address = in.readString();
        pharm_night = in.readString();
        pharm_s_day = in.readString();
        pharm_e_day = in.readString();
        pharm_s_time = in.readString();
        pharm_e_time = in.readString();

        pharm_insurance = in.readString();
//        pharm_24 = in.readString();

    }

    public static final Creator<LatLongPojo> CREATOR = new Creator<LatLongPojo>() {
        @Override
        public LatLongPojo createFromParcel(Parcel in) {
            return new LatLongPojo(in);
        }

        @Override
        public LatLongPojo[] newArray(int size) {
            return new LatLongPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(pharm_id);
        dest.writeString(pharm_name);
        dest.writeDouble(pharm_lat);
        dest.writeDouble(pharm_lng);
        dest.writeString(pharm_mb_no);
        dest.writeDouble(pharm_distance);
        dest.writeString(pharm_address);
        dest.writeString(pharm_night);
        dest.writeString(pharm_s_day);
        dest.writeString(pharm_e_day);
        dest.writeString(pharm_s_time);
        dest.writeString(pharm_e_time);

        dest.writeString(pharm_insurance);
//        dest.writeString(pharm_24);
    }

    public String getPharm_id() {
        return pharm_id;
    }

    public void setPharm_id(String pharm_id) {
        this.pharm_id = pharm_id;
    }*/



    @SerializedName("pharm_id")
    @Expose
    private String pharmId;
    @SerializedName("pharm_name")
    @Expose
    private String pharmName;
    @SerializedName("pharm_owner")
    @Expose
    private String pharmOwner;
    @SerializedName("pharm_mb_no")
    @Expose
    private String pharmMbNo;
    @SerializedName("pharm_address")
    @Expose
    private String pharmAddress;
    @SerializedName("pharm_s_day")
    @Expose
    private String pharmSDay;
    @SerializedName("pharm_e_day")
    @Expose
    private String pharmEDay;
    @SerializedName("pharm_s_time")
    @Expose
    private String pharmSTime;
    @SerializedName("pharm_e_time")
    @Expose
    private String pharmETime;
    @SerializedName("pharm_insurance")
    @Expose
    private String pharmInsurance;
    @SerializedName("pharm_night")
    @Expose
    private String pharmNight;
    @SerializedName("pharm_lat")
    @Expose
    private String pharmLat;
    @SerializedName("pharm_lng")
    @Expose
    private String pharmLng;
    @SerializedName("pharm_distance")
    @Expose
    private String pharmDistance;

    public String getPharmId() {
        return pharmId;
    }

    public void setPharmId(String pharmId) {
        this.pharmId = pharmId;
    }

    public String getPharmName() {
        return pharmName;
    }

    public void setPharmName(String pharmName) {
        this.pharmName = pharmName;
    }

    public String getPharmOwner() {
        return pharmOwner;
    }

    public void setPharmOwner(String pharmOwner) {
        this.pharmOwner = pharmOwner;
    }

    public String getPharmMbNo() {
        return pharmMbNo;
    }

    public void setPharmMbNo(String pharmMbNo) {
        this.pharmMbNo = pharmMbNo;
    }

    public String getPharmAddress() {
        return pharmAddress;
    }

    public void setPharmAddress(String pharmAddress) {
        this.pharmAddress = pharmAddress;
    }

    public String getPharmSDay() {
        return pharmSDay;
    }

    public void setPharmSDay(String pharmSDay) {
        this.pharmSDay = pharmSDay;
    }

    public String getPharmEDay() {
        return pharmEDay;
    }

    public void setPharmEDay(String pharmEDay) {
        this.pharmEDay = pharmEDay;
    }

    public String getPharmSTime() {
        return pharmSTime;
    }

    public void setPharmSTime(String pharmSTime) {
        this.pharmSTime = pharmSTime;
    }

    public String getPharmETime() {
        return pharmETime;
    }

    public void setPharmETime(String pharmETime) {
        this.pharmETime = pharmETime;
    }

    public String getPharmInsurance() {
        return pharmInsurance;
    }

    public void setPharmInsurance(String pharmInsurance) {
        this.pharmInsurance = pharmInsurance;
    }

    public String getPharmNight() {
        return pharmNight;
    }

    public void setPharmNight(String pharmNight) {
        this.pharmNight = pharmNight;
    }

    public String getPharmLat() {
        return pharmLat;
    }

    public void setPharmLat(String pharmLat) {
        this.pharmLat = pharmLat;
    }

    public String getPharmLng() {
        return pharmLng;
    }

    public void setPharmLng(String pharmLng) {
        this.pharmLng = pharmLng;
    }

    public String getPharmDistance() {
        return pharmDistance;
    }

    public void setPharmDistance(String pharmDistance) {
        this.pharmDistance = pharmDistance;
    }

}
