package com.example.smartgpsattendance.Model;

public class AdminModel
{
    String profilePhoto;
    String AdminName;
    String AdminPhoneNumber;

    public AdminModel() {
    }

    public AdminModel(String profilePhoto, String adminName, String adminPhoneNumber) {
        this.profilePhoto = profilePhoto;
        AdminName = adminName;
        AdminPhoneNumber = adminPhoneNumber;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getAdminPhoneNumber() {
        return AdminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        AdminPhoneNumber = adminPhoneNumber;
    }
}
