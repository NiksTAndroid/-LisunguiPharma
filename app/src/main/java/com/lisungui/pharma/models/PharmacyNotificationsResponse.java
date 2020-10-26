package com.lisungui.pharma.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PharmacyNotificationsResponse {

@SerializedName("notification")
@Expose
private List<Notification> notification = null;
@SerializedName("status")
@Expose
private Integer status;

public List<Notification> getNotification() {
return notification;
}

public void setNotification(List<Notification> notification) {
this.notification = notification;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}
    public class Notification {

        @SerializedName("phar_noti_id")
        @Expose
        private String pharNotiId;
        @SerializedName("phar_noti_pharm_id")
        @Expose
        private String pharNotiPharmId;
        @SerializedName("phar_order_id")
        @Expose
        private String pharOrderId;
        @SerializedName("phar_noti_msg")
        @Expose
        private String pharNotiMsg;
        @SerializedName("phar_noti_status")
        @Expose
        private String pharNotiStatus;
        @SerializedName("phar_noti_date")
        @Expose
        private String pharNotiDate;
        @SerializedName("notifi_img")
        @Expose
        private String notifiImg;
        @SerializedName("phar_noti_french_msg")
        @Expose
        private String french_msg;

        public String getPharNotiId() {
            return pharNotiId;
        }

        public void setPharNotiId(String pharNotiId) {
            this.pharNotiId = pharNotiId;
        }

        public String getPharNotiPharmId() {
            return pharNotiPharmId;
        }

        public void setPharNotiPharmId(String pharNotiPharmId) {
            this.pharNotiPharmId = pharNotiPharmId;
        }

        public String getPharOrderId() {
            return pharOrderId;
        }

        public void setPharOrderId(String pharOrderId) {
            this.pharOrderId = pharOrderId;
        }

        public String getPharNotiMsg() {
            return pharNotiMsg;
        }

        public void setPharNotiMsg(String pharNotiMsg) {
            this.pharNotiMsg = pharNotiMsg;
        }

        public String getPharNotiStatus() {
            return pharNotiStatus;
        }

        public void setPharNotiStatus(String pharNotiStatus) {
            this.pharNotiStatus = pharNotiStatus;
        }

        public String getPharNotiDate() {
            return pharNotiDate;
        }

        public void setPharNotiDate(String pharNotiDate) {
            this.pharNotiDate = pharNotiDate;
        }

        public String getNotifiImg() {
            return notifiImg;
        }

        public void setNotifiImg(String notifiImg) {
            this.notifiImg = notifiImg;
        }

        public String getFrench_msg() {
            return french_msg;
        }

        public void setFrench_msg(String french_msg) {
            this.french_msg = french_msg;
        }
    }
}

