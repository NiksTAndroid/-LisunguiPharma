package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityPojo {

    @SerializedName("city")
    @Expose
    private List<CityPojoArray> city = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<CityPojoArray> getCity() {
        return city;
    }

    public void setCity(List<CityPojoArray> city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
