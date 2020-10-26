package com.lisungui.pharma.models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PharmaOrderDetails {

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



    public class OrderDatum {

        @SerializedName("detail_id")
        @Expose
        private String detailId;
        @SerializedName("detail_order_id")
        @Expose
        private String detailOrderId;
        @SerializedName("detail_med_id")
        @Expose
        private String detailMedId;
        @SerializedName("detail_med_price")
        @Expose
        private String detailMedPrice;
        @SerializedName("detail_med_qnty")
        @Expose
        private String detailMedQnty;
        @SerializedName("detail_create_date")
        @Expose
        private String detailCreateDate;
        @SerializedName("detail_update_date")
        @Expose
        private String detailUpdateDate;
        @SerializedName("detail_create_by")
        @Expose
        private String detailCreateBy;
        @SerializedName("detail_update_by")
        @Expose
        private String detailUpdateBy;
        @SerializedName("detail_status")
        @Expose
        private String detailStatus;
        @SerializedName("med_id")
        @Expose
        private String medId;
        @SerializedName("med_name")
        @Expose
        private String medName;
        @SerializedName("med_price")
        @Expose
        private String medPrice;
        @SerializedName("med_pharm_id")
        @Expose
        private String medPharmId;
        @SerializedName("med_type")
        @Expose
        private String medType;
        @SerializedName("med_exp_date")
        @Expose
        private String medExpDate;
        @SerializedName("med_manuf_date")
        @Expose
        private String medManufDate;
        @SerializedName("med_create_date")
        @Expose
        private String medCreateDate;
        @SerializedName("med_update_date")
        @Expose
        private String medUpdateDate;
        @SerializedName("med_create_by")
        @Expose
        private String medCreateBy;
        @SerializedName("med_update_by")
        @Expose
        private String medUpdateBy;
        @SerializedName("med_status")
        @Expose
        private String medStatus;
        @SerializedName("pharm_id")
        @Expose
        private Object pharmId;
        @SerializedName("pharm_name")
        @Expose
        private Object pharmName;

        public String getDetailId() {
            return detailId;
        }

        public void setDetailId(String detailId) {
            this.detailId = detailId;
        }

        public String getDetailOrderId() {
            return detailOrderId;
        }

        public void setDetailOrderId(String detailOrderId) {
            this.detailOrderId = detailOrderId;
        }

        public String getDetailMedId() {
            return detailMedId;
        }

        public void setDetailMedId(String detailMedId) {
            this.detailMedId = detailMedId;
        }

        public String getDetailMedPrice() {
            return detailMedPrice;
        }

        public void setDetailMedPrice(String detailMedPrice) {
            this.detailMedPrice = detailMedPrice;
        }

        public String getDetailMedQnty() {
            return detailMedQnty;
        }

        public void setDetailMedQnty(String detailMedQnty) {
            this.detailMedQnty = detailMedQnty;
        }

        public String getDetailCreateDate() {
            return detailCreateDate;
        }

        public void setDetailCreateDate(String detailCreateDate) {
            this.detailCreateDate = detailCreateDate;
        }

        public String getDetailUpdateDate() {
            return detailUpdateDate;
        }

        public void setDetailUpdateDate(String detailUpdateDate) {
            this.detailUpdateDate = detailUpdateDate;
        }

        public String getDetailCreateBy() {
            return detailCreateBy;
        }

        public void setDetailCreateBy(String detailCreateBy) {
            this.detailCreateBy = detailCreateBy;
        }

        public String getDetailUpdateBy() {
            return detailUpdateBy;
        }

        public void setDetailUpdateBy(String detailUpdateBy) {
            this.detailUpdateBy = detailUpdateBy;
        }

        public String getDetailStatus() {
            return detailStatus;
        }

        public void setDetailStatus(String detailStatus) {
            this.detailStatus = detailStatus;
        }

        public String getMedId() {
            return medId;
        }

        public void setMedId(String medId) {
            this.medId = medId;
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

        public String getMedPharmId() {
            return medPharmId;
        }

        public void setMedPharmId(String medPharmId) {
            this.medPharmId = medPharmId;
        }

        public String getMedType() {
            return medType;
        }

        public void setMedType(String medType) {
            this.medType = medType;
        }

        public String getMedExpDate() {
            return medExpDate;
        }

        public void setMedExpDate(String medExpDate) {
            this.medExpDate = medExpDate;
        }

        public String getMedManufDate() {
            return medManufDate;
        }

        public void setMedManufDate(String medManufDate) {
            this.medManufDate = medManufDate;
        }

        public String getMedCreateDate() {
            return medCreateDate;
        }

        public void setMedCreateDate(String medCreateDate) {
            this.medCreateDate = medCreateDate;
        }

        public String getMedUpdateDate() {
            return medUpdateDate;
        }

        public void setMedUpdateDate(String medUpdateDate) {
            this.medUpdateDate = medUpdateDate;
        }

        public String getMedCreateBy() {
            return medCreateBy;
        }

        public void setMedCreateBy(String medCreateBy) {
            this.medCreateBy = medCreateBy;
        }

        public String getMedUpdateBy() {
            return medUpdateBy;
        }

        public void setMedUpdateBy(String medUpdateBy) {
            this.medUpdateBy = medUpdateBy;
        }

        public String getMedStatus() {
            return medStatus;
        }

        public void setMedStatus(String medStatus) {
            this.medStatus = medStatus;
        }

        public Object getPharmId() {
            return pharmId;
        }

        public void setPharmId(Object pharmId) {
            this.pharmId = pharmId;
        }

        public Object getPharmName() {
            return pharmName;
        }

        public void setPharmName(Object pharmName) {
            this.pharmName = pharmName;
        }

    }
//-----------------------------------com.example.OrderDetail.java-----------------------------------



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
        private List<OrderDatum> orderData = null;
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
        @SerializedName("order_pharma_data")
        @Expose
        private List<OrderDatum> orderPharmaData = null;
        @SerializedName("order_pharma_desc")
        @Expose
        private Object orderPharmaDesc;
        @SerializedName("order_pharm_id")
        @Expose
        private String orderPharmId;
        @SerializedName("accepted_pharm_details")
        @Expose
        private List<Object> acceptedPharmDetails = null;

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

        public List<OrderDatum> getOrderData() {
            return orderData;
        }

        public void setOrderData(List<OrderDatum> orderData) {
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

        public List<OrderDatum> getOrderPharmaData() {
            return orderPharmaData;
        }

        public void setOrderPharmaData(List<OrderDatum> orderPharmaData) {
            this.orderPharmaData = orderPharmaData;
        }

        public Object getOrderPharmaDesc() {
            return orderPharmaDesc;
        }

        public void setOrderPharmaDesc(Object orderPharmaDesc) {
            this.orderPharmaDesc = orderPharmaDesc;
        }

        public String getOrderPharmId() {
            return orderPharmId;
        }

        public void setOrderPharmId(String orderPharmId) {
            this.orderPharmId = orderPharmId;
        }

        public List<Object> getAcceptedPharmDetails() {
            return acceptedPharmDetails;
        }

        public void setAcceptedPharmDetails(List<Object> acceptedPharmDetails) {
            this.acceptedPharmDetails = acceptedPharmDetails;
        }

    }

}
