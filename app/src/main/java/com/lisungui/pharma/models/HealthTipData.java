package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthTipData {

    @SerializedName("healthtip")
    @Expose
    private List<HealthTipDataDetails> healthtip = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<HealthTipDataDetails> getHealthtip() {
        return healthtip;
    }

    public void setHealthtip(List<HealthTipDataDetails> healthtip) {
        this.healthtip = healthtip;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
