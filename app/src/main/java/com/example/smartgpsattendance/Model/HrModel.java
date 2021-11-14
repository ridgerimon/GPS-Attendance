package com.example.smartgpsattendance.Model;

public class HrModel {
    String full_name_txt;
    String email_txt;
    String mobile_txt;
    String HrID_txt;
    String profilePhoto;
    String uId;
    String token;

    public HrModel() {
    }

    public HrModel(String full_name_txt, String email_txt, String mobile_txt, String hrID_txt, String profilePhoto, String uId, String token) {
        this.full_name_txt = full_name_txt;
        this.email_txt = email_txt;
        this.mobile_txt = mobile_txt;
        this.HrID_txt = hrID_txt;
        this.profilePhoto = profilePhoto;
        this.uId = uId;
        this.token = token;
    }

    public String getHrID_txt() {
        return HrID_txt;
    }

    public void setHrID_txt(String hrID_txt) {
        HrID_txt = hrID_txt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFull_name_txt() {
        return full_name_txt;
    }

    public void setFull_name_txt(String full_name_txt) {
        this.full_name_txt = full_name_txt;
    }

    public String getEmail_txt() {
        return email_txt;
    }

    public void setEmail_txt(String email_txt) {
        this.email_txt = email_txt;
    }

    public String getMobile_txt() {
        return mobile_txt;
    }

    public void setMobile_txt(String mobile_txt) {
        this.mobile_txt = mobile_txt;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
