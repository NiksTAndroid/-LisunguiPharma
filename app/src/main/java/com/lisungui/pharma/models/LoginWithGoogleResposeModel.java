package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginWithGoogleResposeModel {

    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public class User {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_gcm_key")
        @Expose
        private String userGcmKey;
        @SerializedName("user_google_id")
        @Expose
        private String userGoogleId;
        @SerializedName("user_ven_code")
        @Expose
        private String userVenCode;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_gender")
        @Expose
        private Object userGender;
        @SerializedName("user_email_id")
        @Expose
        private String userEmailId;
        @SerializedName("user_country_code")
        @Expose
        private Object userCountryCode;
        @SerializedName("user_mb_no")
        @Expose
        private String userMbNo;
        @SerializedName("user_password")
        @Expose
        private String userPassword;
        @SerializedName("user_full_name")
        @Expose
        private String userFullName;
        @SerializedName("user_address")
        @Expose
        private String userAddress;
        @SerializedName("user_ins_comp_name")
        @Expose
        private String userInsCompName;
        @SerializedName("user_img")
        @Expose
        private String userImg;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_create_date")
        @Expose
        private String userCreateDate;
        @SerializedName("user_update_date")
        @Expose
        private String userUpdateDate;
        @SerializedName("user_create_by")
        @Expose
        private String userCreateBy;
        @SerializedName("user_update_by")
        @Expose
        private String userUpdateBy;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserGcmKey() {
            return userGcmKey;
        }

        public void setUserGcmKey(String userGcmKey) {
            this.userGcmKey = userGcmKey;
        }

        public String getUserGoogleId() {
            return userGoogleId;
        }

        public void setUserGoogleId(String userGoogleId) {
            this.userGoogleId = userGoogleId;
        }

        public String getUserVenCode() {
            return userVenCode;
        }

        public void setUserVenCode(String userVenCode) {
            this.userVenCode = userVenCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Object getUserGender() {
            return userGender;
        }

        public void setUserGender(Object userGender) {
            this.userGender = userGender;
        }

        public String getUserEmailId() {
            return userEmailId;
        }

        public void setUserEmailId(String userEmailId) {
            this.userEmailId = userEmailId;
        }

        public Object getUserCountryCode() {
            return userCountryCode;
        }

        public void setUserCountryCode(Object userCountryCode) {
            this.userCountryCode = userCountryCode;
        }

        public String getUserMbNo() {
            return userMbNo;
        }

        public void setUserMbNo(String userMbNo) {
            this.userMbNo = userMbNo;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserInsCompName() {
            return userInsCompName;
        }

        public void setUserInsCompName(String userInsCompName) {
            this.userInsCompName = userInsCompName;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserCreateDate() {
            return userCreateDate;
        }

        public void setUserCreateDate(String userCreateDate) {
            this.userCreateDate = userCreateDate;
        }

        public String getUserUpdateDate() {
            return userUpdateDate;
        }

        public void setUserUpdateDate(String userUpdateDate) {
            this.userUpdateDate = userUpdateDate;
        }

        public String getUserCreateBy() {
            return userCreateBy;
        }

        public void setUserCreateBy(String userCreateBy) {
            this.userCreateBy = userCreateBy;
        }

        public String getUserUpdateBy() {
            return userUpdateBy;
        }

        public void setUserUpdateBy(String userUpdateBy) {
            this.userUpdateBy = userUpdateBy;
        }

    }
}
