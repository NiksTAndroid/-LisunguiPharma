package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class OrderDetailsResponce {

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
        private Object medName;
        @SerializedName("med_price")
        @Expose
        private Object medPrice;
        @SerializedName("med_qnty")
        @Expose
        private Object medQnty;
        @SerializedName("total_med_price")
        @Expose
        private Object totalMedPrice;
        @SerializedName("order_pharma_data")
        @Expose
        private List<OrderPharmaDatum> orderPharmaData = null;
        @SerializedName("order_pharma_desc")
        @Expose
        private List<OrderPharmaDesc> orderPharmaDesc = null;

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
            return String.valueOf(medName);
        }

        public void setMedName(Object medName) {
            this.medName = medName;
        }

        public Object getMedPrice() {
            return medPrice;
        }

        public void setMedPrice(Object medPrice) {
            this.medPrice = medPrice;
        }

        public Object getMedQnty() {
            return medQnty;
        }

        public void setMedQnty(Object medQnty) {
            this.medQnty = medQnty;
        }

        public Object getTotalMedPrice() {
            return totalMedPrice;
        }

        public void setTotalMedPrice(Object totalMedPrice) {
            this.totalMedPrice = totalMedPrice;
        }

        public List<OrderPharmaDatum> getOrderPharmaData() {
            return orderPharmaData;
        }

        public void setOrderPharmaData(List<OrderPharmaDatum> orderPharmaData) {
            this.orderPharmaData = orderPharmaData;
        }

        public List<OrderPharmaDesc> getOrderPharmaDesc() {
            return orderPharmaDesc;
        }

        public void setOrderPharmaDesc(List<OrderPharmaDesc> orderPharmaDesc) {
            this.orderPharmaDesc = orderPharmaDesc;
        }
    }


    public class OrderPharmaDatum {

        @SerializedName("pharm_id")
        @Expose
        private String pharmId;
        @SerializedName("pharm_name")
        @Expose
        private String pharmName;
        @SerializedName("pharm_mb_no")
        @Expose
        private String pharmMbNo;
        @SerializedName("pharm_owner")
        @Expose
        private String pharmOwner;
        @SerializedName("pharm_address")
        @Expose
        private String pharmAddress;
        @SerializedName("pharm_24")
        @Expose
        private String pharm24;
        @SerializedName("pharm_night")
        @Expose
        private String pharmNight;

        public String getPharmId() {
            return pharmId;
        }

        public void setPharmId(String pharmId) {
            this.pharmId = pharmId;
        }

        public String getPharmName() {
            return pharmName;
        }

        public void setPharmName(String pharmName) {
            this.pharmName = pharmName;
        }

        public String getPharmMbNo() {
            return pharmMbNo;
        }

        public void setPharmMbNo(String pharmMbNo) {
            this.pharmMbNo = pharmMbNo;
        }

        public String getPharmOwner() {
            return pharmOwner;
        }

        public void setPharmOwner(String pharmOwner) {
            this.pharmOwner = pharmOwner;
        }

        public String getPharmAddress() {
            return pharmAddress;
        }

        public void setPharmAddress(String pharmAddress) {
            this.pharmAddress = pharmAddress;
        }

        public String getPharm24() {
            return pharm24;
        }

        public void setPharm24(String pharm24) {
            this.pharm24 = pharm24;
        }

        public String getPharmNight() {
            return pharmNight;
        }

        public void setPharmNight(String pharmNight) {
            this.pharmNight = pharmNight;
        }

    }


    public class OrderPharmaDesc {

        @SerializedName("desc")
        @Expose
        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }


}
