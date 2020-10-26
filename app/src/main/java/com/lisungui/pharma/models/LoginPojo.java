package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khrishi on 5/1/17.
 */


public class LoginPojo {

    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("msg")
    @Expose
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class User {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_gcm_key")
        @Expose
        private String userGcmKey;
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
        private String userCountryCode;
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

        public String getUserCountryCode() {
            return userCountryCode;
        }

        public void setUserCountryCode(String userCountryCode) {
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


    // pharmacy

        @SerializedName("pharm_id")
        @Expose
        private String pharmId;
        @SerializedName("pharm_username")
        @Expose
        private String pharmUsername;
        @SerializedName("pharm_password")
        @Expose
        private String pharmPassword;
        @SerializedName("pharm_name")
        @Expose
        private String pharmName;
        @SerializedName("pharm_mb_no")
        @Expose
        private String pharmMbNo;
        @SerializedName("pharm_owner")
        @Expose
        private String pharmOwner;
        @SerializedName("pharm_title")
        @Expose
        private String pharmTitle;
        @SerializedName("pharm_address")
        @Expose
        private String pharmAddress;
        @SerializedName("pharm_s_day")
        @Expose
        private String pharmSDay;
        @SerializedName("pharm_e_day")
        @Expose
        private String pharmEDay;
        @SerializedName("pharm_s_time")
        @Expose
        private String pharmSTime;
        @SerializedName("pharm_e_time")
        @Expose
        private String pharmETime;
        @SerializedName("pharm_insurance")
        @Expose
        private String pharmInsurance;
        @SerializedName("pharm_night")
        @Expose
        private String pharmNight;
        @SerializedName("pharm_24")
        @Expose
        private String pharm24;
        @SerializedName("pharm_img")
        @Expose
        private String pharmImg;
        @SerializedName("pharm_lat")
        @Expose
        private String pharmLat;
        @SerializedName("pharm_lng")
        @Expose
        private String pharmLng;
        @SerializedName("pharm_status")
        @Expose
        private String pharmStatus;

        public String getPharmId() {
            return pharmId;
        }

        public void setPharmId(String pharmId) {
            this.pharmId = pharmId;
        }

        public String getPharmUsername() {
            return pharmUsername;
        }

        public void setPharmUsername(String pharmUsername) {
            this.pharmUsername = pharmUsername;
        }

        public String getPharmPassword() {
            return pharmPassword;
        }

        public void setPharmPassword(String pharmPassword) {
            this.pharmPassword = pharmPassword;
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

        public String getPharmTitle() {
            return pharmTitle;
        }

        public void setPharmTitle(String pharmTitle) {
            this.pharmTitle = pharmTitle;
        }

        public String getPharmAddress() {
            return pharmAddress;
        }

        public void setPharmAddress(String pharmAddress) {
            this.pharmAddress = pharmAddress;
        }

        public String getPharmSDay() {
            return pharmSDay;
        }

        public void setPharmSDay(String pharmSDay) {
            this.pharmSDay = pharmSDay;
        }

        public String getPharmEDay() {
            return pharmEDay;
        }

        public void setPharmEDay(String pharmEDay) {
            this.pharmEDay = pharmEDay;
        }

        public String getPharmSTime() {
            return pharmSTime;
        }

        public void setPharmSTime(String pharmSTime) {
            this.pharmSTime = pharmSTime;
        }

        public String getPharmETime() {
            return pharmETime;
        }

        public void setPharmETime(String pharmETime) {
            this.pharmETime = pharmETime;
        }

        public String getPharmInsurance() {
            return pharmInsurance;
        }

        public void setPharmInsurance(String pharmInsurance) {
            this.pharmInsurance = pharmInsurance;
        }

        public String getPharmNight() {
            return pharmNight;
        }

        public void setPharmNight(String pharmNight) {
            this.pharmNight = pharmNight;
        }

        public String getPharm24() {
            return pharm24;
        }

        public void setPharm24(String pharm24) {
            this.pharm24 = pharm24;
        }

        public String getPharmImg() {
            return pharmImg;
        }

        public void setPharmImg(String pharmImg) {
            this.pharmImg = pharmImg;
        }

        public String getPharmLat() {
            return pharmLat;
        }

        public void setPharmLat(String pharmLat) {
            this.pharmLat = pharmLat;
        }

        public String getPharmLng() {
            return pharmLng;
        }

        public void setPharmLng(String pharmLng) {
            this.pharmLng = pharmLng;
        }

        public String getPharmStatus() {
            return pharmStatus;
        }

        public void setPharmStatus(String pharmStatus) {
            this.pharmStatus = pharmStatus;
        }


    }

}

/*
public class LoginPojo {
    private Integer status;

    ArrayList<User> user = new ArrayList<>();

    public ArrayList<User> getUser() {
        return user;
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class User {

        private int user_id;
        private String user_name;
        private String user_email_id;
        private String user_gender;
        private String user_mb_no;
        private String user_address;

        private String user_type;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_email_id() {
            return user_email_id;
        }

        public void setUser_email_id(String user_email_id) {
            this.user_email_id = user_email_id;
        }

        public String getUser_gender() {
            return user_gender;
        }

        public void setUser_gender(String user_gender) {
            this.user_gender = user_gender;
        }

        public String getUser_mb_no() {
            return user_mb_no;
        }

        public void setUser_mb_no(String user_mb_no) {
            this.user_mb_no = user_mb_no;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}
*/
