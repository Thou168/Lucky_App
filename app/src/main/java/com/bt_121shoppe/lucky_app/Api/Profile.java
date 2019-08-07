package com.bt_121shoppe.lucky_app.Api;

public class Profile {
    private int id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String gender;
    private String date_of_birth;
    private String telephone;
    private String address;
    private String shop_name;
    private String responsible_officer;
    private String job;
    private String profile_photo;
    private String cover_photo;
    private String created;
    private String modified;
    private String user_status;
    private String record_status;
    private String province;
    private String marital;
    private String marital_status;
    private String shop_address;
    private String wing_account_number;
    private String wing_account_name;
    private String place_of_birth;
    private String status;
    private String base64_profile_image;
    private String base64_cover_image;
    private int group;

    private int[] groups;

//    Profile( int id, String username,String email,String first_name, String last_name,
//             String gender,String date_of_birth,String telephone, String address, String shop_name,
//             String responsible_officer,String job, String profile_photo, String cover_photo,
//             String created, String modified,String user_status,String record_status, String province,
//             String marital, String shop_address, String wing_account_number, String wing_account_name,
//             String place_of_birth, String status){
//        this.id=id;
//        this.username=username;
//        this.email=email;
//        this.first_name=first_name;
//        this.last_name=last_name;
//        this. gender=gender;
//        this.date_of_birth=date_of_birth;
//        this. telephone=telephone;
//        this.address=address;
//        this.shop_name=shop_name;
//        this.responsible_officer=responsible_officer;
//        this.job=job;
//        this.profile_photo=profile_photo;
//        this. cover_photo=cover_photo;
//        this.created=created;
//        this.modified=modified;
//        this.user_status=user_status;
//        this.record_status=record_status;
//        this.province=province;
//        this.marital=marital;
//        this.shop_address=shop_address;
//        this.wing_account_number=wing_account_number;
//        this. wing_account_name=wing_account_name;
//        this.place_of_birth=place_of_birth;
//        this.status=status;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMarital_status() {        return marital_status;    }

    public void setMarital_status(String marital_status) {     this.marital_status = marital_status;   }

    public String getBase64_cover_image() {     return base64_cover_image;  }

    public void setBase64_cover_image(String base64_cover_image) {    this.base64_cover_image = base64_cover_image;   }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getResponsible_officer() {
        return responsible_officer;
    }

    public void setResponsible_officer(String responsible_officer) {
        this.responsible_officer = responsible_officer;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getRecord_status() {
        return record_status;
    }

    public void setRecord_status(String record_status) {
        this.record_status = record_status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getWing_account_number() {
        return wing_account_number;
    }

    public void setWing_account_number(String wing_account_number) {
        this.wing_account_number = wing_account_number;
    }

    public String getWing_account_name() {
        return wing_account_name;
    }

    public void setWing_account_name(String wing_account_name) {
        this.wing_account_name = wing_account_name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int[] getGroups() {
        return groups;
    }

    public void setGroups(int[] groups) {
        this.groups = groups;
    }

    public String getBase64_cover_photo_image() {
        return base64_cover_image;
    }

    public String getBase64_profile_image() {
        return base64_profile_image;
    }

    public void setBase64_cover_photo_image(String base64_cover_photo_image) {
        this.base64_cover_image = base64_cover_image;
    }

    public void setBase64_profile_image(String base64_profile_image) {
        this.base64_profile_image = base64_profile_image;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
