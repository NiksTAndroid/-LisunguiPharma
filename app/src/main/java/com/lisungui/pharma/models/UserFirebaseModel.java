package com.lisungui.pharma.models;


public class UserFirebaseModel {
    private String userID;
    private String type;
    private boolean userStatus;
    private String userFCM;
    private String userProfilePic;
    private String userLastLogin;
    private String userName;

    public UserFirebaseModel(String userID, String type, boolean userStatus, String userFCM, String userProfilePic, String userLastLogin, String userName, boolean isActive) {
        this.userID = userID;
        this.type = type;
        this.userStatus = userStatus;
        this.userFCM = userFCM;
        this.userProfilePic = userProfilePic;
        this.userLastLogin = userLastLogin;
        this.userName = userName;
        this.isActive = isActive;
    }

    private boolean isActive;

    public UserFirebaseModel() {
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getUserFCM() {
        return userFCM;
    }

    public void setUserFCM(String userFCM) {
        this.userFCM = userFCM;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserLastLogin() {
        return userLastLogin;
    }

    public void setUserLastLogin(String userLastLogin) {
        this.userLastLogin = userLastLogin;
    }


    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
