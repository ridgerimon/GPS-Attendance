package com.example.smartgpsattendance.Model;

public class EmployeeModel
{
    String full_name_txt;
    String email_txt; 
    String mobile_txt; 
    String jop_title_txt; 
    String jop_type_txt; 
    String employeeID_txt;
    String profilePhoto;
    String uId;

    public EmployeeModel() {
    }

    public EmployeeModel(String full_name_txt, String email_txt, String mobile_txt, String jop_title_txt, String jop_type_txt, String employeeID_txt, String profilePhoto, String uId) {
        this.full_name_txt = full_name_txt;
        this.email_txt = email_txt;
        this.mobile_txt = mobile_txt;
        this.jop_title_txt = jop_title_txt;
        this.jop_type_txt = jop_type_txt;
        this.employeeID_txt = employeeID_txt;
        this.profilePhoto = profilePhoto;
        this.uId = uId;
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

    public String getJop_title_txt() {
        return jop_title_txt;
    }

    public void setJop_title_txt(String jop_title_txt) {
        this.jop_title_txt = jop_title_txt;
    }

    public String getJop_type_txt() {
        return jop_type_txt;
    }

    public void setJop_type_txt(String jop_type_txt) {
        this.jop_type_txt = jop_type_txt;
    }

    public String getEmployeeID_txt() {
        return employeeID_txt;
    }

    public void setEmployeeID_txt(String employeeID_txt) {
        this.employeeID_txt = employeeID_txt;
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
