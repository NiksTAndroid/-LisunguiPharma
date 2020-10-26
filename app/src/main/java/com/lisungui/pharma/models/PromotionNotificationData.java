package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionNotificationData {

    @SerializedName("pharm_notification")
    @Expose
    private List<PromotionNotificationDetails> pharmNotification = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<PromotionNotificationDetails> getPharmNotification() {
        return pharmNotification;
    }

    public void setPharmNotification(List<PromotionNotificationDetails> pharmNotification) {
        this.pharmNotification = pharmNotification;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
