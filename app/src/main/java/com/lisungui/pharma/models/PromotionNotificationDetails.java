package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotionNotificationDetails {

    @SerializedName("prom_noti_id")
    @Expose
    private String promNotiId;
    @SerializedName("prom_noti_msg")
    @Expose
    private String promNotiMsg;
    @SerializedName("prom_noti_date")
    @Expose
    private String promNotiDate;

    @SerializedName("prom_noti_img")
    @Expose
    private String promNotiImg;

    public String getPromNotiId() {
        return promNotiId;
    }

    public void setPromNotiId(String promNotiId) {
        this.promNotiId = promNotiId;
    }

    public String getPromNotiMsg() {
        return promNotiMsg;
    }

    public void setPromNotiMsg(String promNotiMsg) {
        this.promNotiMsg = promNotiMsg;
    }

    public String getPromNotiDate() {
        return promNotiDate;
    }

    public void setPromNotiDate(String promNotiDate) {
        this.promNotiDate = promNotiDate;
    }

    public String getPromNotiImg() {
        return promNotiImg;
    }

    public void setPromNotiImg(String promNotiImg) {
        this.promNotiImg = promNotiImg;
    }

}
