package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderNotificationDetails {

    @SerializedName("ornot_id")
    @Expose
    private String ornotId;
    @SerializedName("ornot_user_id")
    @Expose
    private String ornotUserId;
    @SerializedName("ornot_message")
    @Expose
    private String ornotMessage;
    @SerializedName("ornot_mb_no")
    @Expose
    private String ornotMbNo;
    @SerializedName("ornot_status")
    @Expose
    private String ornotStatus;
    @SerializedName("ornot_date")
    @Expose
    private String ornotDate;

    @SerializedName("ornot_order_id")
    @Expose
    private String ornotOrderId;

    @SerializedName("ornot_order_desc")
    @Expose
    private String ornotOrderDesc;

    @SerializedName("ornot_french_msg")
    @Expose
    private String french_msg;

    public String getOrnotId() {
        return ornotId;
    }

    public void setOrnotId(String ornotId) {
        this.ornotId = ornotId;
    }

    public String getOrnotUserId() {
        return ornotUserId;
    }

    public void setOrnotUserId(String ornotUserId) {
        this.ornotUserId = ornotUserId;
    }

    public String getOrnotMessage() {
        return ornotMessage;
    }

    public void setOrnotMessage(String ornotMessage) {
        this.ornotMessage = ornotMessage;
    }

    public String getOrnotMbNo() {
        return ornotMbNo;
    }

    public void setOrnotMbNo(String ornotMbNo) {
        this.ornotMbNo = ornotMbNo;
    }

    public String getOrnotStatus() {
        return ornotStatus;
    }

    public void setOrnotStatus(String ornotStatus) {
        this.ornotStatus = ornotStatus;
    }

    public String getOrnotDate() {
        return ornotDate;
    }

    public void setOrnotDate(String ornotDate) {
        this.ornotDate = ornotDate;
    }

    public String getOrnotOrderId() {
        return ornotOrderId;
    }

    public void setOrnotOrderId(String ornotOrderId) {
        this.ornotOrderId = ornotOrderId;
    }

    public String getOrnotOrderDesc() {
        return ornotOrderDesc;
    }

    public void setOrnotOrderDesc(String ornotOrderDesc) {
        this.ornotOrderDesc = ornotOrderDesc;
    }

    public String getFrench_msg() {
        return french_msg;
    }

    public void setFrench_msg(String french_msg) {
        this.french_msg = french_msg;
    }
}
