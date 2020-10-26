package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryMedicineList {
    @SerializedName("medc_id")
    @Expose
    private String medcId;
    @SerializedName("medc_name")
    @Expose
    private String medcName;
    @SerializedName("medc_price")
    @Expose
    private String medcPrice;
    @SerializedName("pharm_id")
    @Expose
    private String pharmId;
    @SerializedName("pharm_name")
    @Expose
    private String pharmName;
    @SerializedName("country")
    @Expose
    private String country;

    public String getMedcId() {
        return medcId;
    }

    public void setMedcId(String medcId) {
        this.medcId = medcId;
    }

    public String getMedcName() {
        return medcName;
    }

    public void setMedcName(String medcName) {
        this.medcName = medcName;
    }

    public String getMedcPrice() {
        return medcPrice;
    }

    public void setMedcPrice(String medcPrice) {
        this.medcPrice = medcPrice;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
