package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceOrderNotificationData {

    @SerializedName("order_notification")
    @Expose
    private List<PlaceOrderNotificationDetails> orderNotification = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<PlaceOrderNotificationDetails> getOrderNotification() {
        return orderNotification;
    }

    public void setOrderNotification(List<PlaceOrderNotificationDetails> orderNotification) {
        this.orderNotification = orderNotification;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
