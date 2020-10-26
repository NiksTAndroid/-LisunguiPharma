package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthTipDataDetails {

    @SerializedName("tip_id")
    @Expose
    private String tipId;
    @SerializedName("tip_message")
    @Expose
    private String tipMessage;
    @SerializedName("tip_status")
    @Expose
    private String tipStatus;
    @SerializedName("tip_date")
    @Expose
    private String tipDate;

    public String getTipId() {
        return tipId;
    }

    public void setTipId(String tipId) {
        this.tipId = tipId;
    }

    public String getTipMessage() {
        return tipMessage;
    }

    public void setTipMessage(String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public String getTipStatus() {
        return tipStatus;
    }

    public void setTipStatus(String tipStatus) {
        this.tipStatus = tipStatus;
    }

    public String getTipDate() {
        return tipDate;
    }

    public void setTipDate(String tipDate) {
        this.tipDate = tipDate;
    }
}
