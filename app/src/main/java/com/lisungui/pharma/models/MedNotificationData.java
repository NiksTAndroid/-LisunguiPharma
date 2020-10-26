package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedNotificationData {


    @SerializedName("notification")
    @Expose
    private List<MedNotificationDetails> notification = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<MedNotificationDetails> getNotification() {
        return notification;
    }

    public void setNotification(List<MedNotificationDetails> notification) {
        this.notification = notification;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
