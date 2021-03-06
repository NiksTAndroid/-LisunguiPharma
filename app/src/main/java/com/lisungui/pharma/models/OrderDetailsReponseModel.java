package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsReponseModel {


    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public class OrderDetail {

        @SerializedName("order_server_id")
        @Expose
        private String orderServerId;
        @SerializedName("order_qnty")
        @Expose
        private String orderQnty;
        @SerializedName("order_total_price")
        @Expose
        private String orderTotalPrice;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("order_track_status")
        @Expose
        private String orderTrackStatus;
        @SerializedName("order_update_date")
        @Expose
        private String orderUpdateDate;
        @SerializedName("order_data")
        @Expose
        private Object orderData;
        @SerializedName("order_pres_img")
        @Expose
        private String orderPresImg;
        @SerializedName("order_description")
        @Expose
        private String orderDescription;
        @SerializedName("order_username")
        @Expose
        private String orderUsername;
        @SerializedName("order_contact")
        @Expose
        private String orderContact;
        @SerializedName("order_address")
        @Expose
        private String orderAddress;
        @SerializedName("med_name")
        @Expose
        private String medName;
        @SerializedName("med_price")
        @Expose
        private String medPrice;
        @SerializedName("med_qnty")
        @Expose
        private String medQnty;
        @SerializedName("total_med_price")
        @Expose
        private String totalMedPrice;

        public String getOrderServerId() {
            return orderServerId;
        }

        public void setOrderServerId(String orderServerId) {
            this.orderServerId = orderServerId;
        }

        public String getOrderQnty() {
            return orderQnty;
        }

        public void setOrderQnty(String orderQnty) {
            this.orderQnty = orderQnty;
        }

        public String getOrderTotalPrice() {
            return orderTotalPrice;
        }

        public void setOrderTotalPrice(String orderTotalPrice) {
            this.orderTotalPrice = orderTotalPrice;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderTrackStatus() {
            return orderTrackStatus;
        }

        public void setOrderTrackStatus(String orderTrackStatus) {
            this.orderTrackStatus = orderTrackStatus;
        }

        public String getOrderUpdateDate() {
            return orderUpdateDate;
        }

        public void setOrderUpdateDate(String orderUpdateDate) {
            this.orderUpdateDate = orderUpdateDate;
        }

        public Object getOrderData() {
            return orderData;
        }

        public void setOrderData(Object orderData) {
            this.orderData = orderData;
        }

        public String getOrderPresImg() {
            return orderPresImg;
        }

        public void setOrderPresImg(String orderPresImg) {
            this.orderPresImg = orderPresImg;
        }

        public String getOrderDescription() {
            return orderDescription;
        }

        public void setOrderDescription(String orderDescription) {
            this.orderDescription = orderDescription;
        }

        public String getOrderUsername() {
            return orderUsername;
        }

        public void setOrderUsername(String orderUsername) {
            this.orderUsername = orderUsername;
        }

        public String getOrderContact() {
            return orderContact;
        }

        public void setOrderContact(String orderContact) {
            this.orderContact = orderContact;
        }

        public String getOrderAddress() {
            return orderAddress;
        }

        public void setOrderAddress(String orderAddress) {
            this.orderAddress = orderAddress;
        }

        public String getMedName() {
            return medName;
        }

        public void setMedName(String medName) {
            this.medName = medName;
        }

        public String getMedPrice() {
            return medPrice;
        }

        public void setMedPrice(String medPrice) {
            this.medPrice = medPrice;
        }

        public String getMedQnty() {
            return medQnty;
        }

        public void setMedQnty(String medQnty) {
            this.medQnty = medQnty;
        }

        public String getTotalMedPrice() {
            return totalMedPrice;
        }

        public void setTotalMedPrice(String totalMedPrice) {
            this.totalMedPrice = totalMedPrice;
        }

    }
}
