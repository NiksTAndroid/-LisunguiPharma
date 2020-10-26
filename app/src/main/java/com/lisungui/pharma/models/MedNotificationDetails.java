package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedNotificationDetails {

    @SerializedName("notifi_id")
    @Expose
    private String notifiId;
    @SerializedName("notifi_user_id")
    @Expose
    private String notifiUserId;
    @SerializedName("notifi_message")
    @Expose
    private String notifiMessage;
    @SerializedName("notifi_status")
    @Expose
    private String notifiStatus;
    @SerializedName("notifi_date")
    @Expose
    private String notifiDate;
    @SerializedName("notifi_img")
    @Expose
    private String notifiImg;

    public String getNotifiId() {
        return notifiId;
    }

    public void setNotifiId(String notifiId) {
        this.notifiId = notifiId;
    }

    public String getNotifiUserId() {
        return notifiUserId;
    }

    public void setNotifiUserId(String notifiUserId) {
        this.notifiUserId = notifiUserId;
    }

    public String getNotifiMessage() {
        return notifiMessage;
    }

    public void setNotifiMessage(String notifiMessage) {
        this.notifiMessage = notifiMessage;
    }

    public String getNotifiStatus() {
        return notifiStatus;
    }

    public void setNotifiStatus(String notifiStatus) {
        this.notifiStatus = notifiStatus;
    }

    public String getNotifiDate() {
        return notifiDate;
    }

    public void setNotifiDate(String notifiDate) {
        this.notifiDate = notifiDate;
    }

    public String getNotifiImg() {
        return notifiImg;
    }

    public void setNotifiImg(String notifiImg) {
        this.notifiImg = notifiImg;
    }

}
