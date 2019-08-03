package com.bt_121shoppe.lucky_app.Api.api;

import com.google.gson.annotations.SerializedName;

public class User {
        private float id;
        private String username;
        private String email;
        private String first_name;
        private String last_name;
        @SerializedName("profile")
        Profile ProfileObject;


        // Getter Methods

        public float getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public Profile getProfile() {
            return ProfileObject;
        }

        // Setter Methods

        public void setId( float id ) {
            this.id = id;
        }

        public void setUsername( String username ) {
            this.username = username;
        }

        public void setEmail( String email ) {
            this.email = email;
        }

        public void setFirst_name( String first_name ) {
            this.first_name = first_name;
        }

        public void setLast_name( String last_name ) {
            this.last_name = last_name;
        }

        public void setProfile( Profile profileObject ) {
            this.ProfileObject = profileObject;
        }
    public class Profile {
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
        private float user_status;
        private float record_status;
        private float province;
        private String marital_status;
        private String shop_address;
        private String wing_account_number;
        private String wing_account_name;
        private float place_of_birth;
        private String base64_profile_image;
        @SerializedName("base64_cover_image")
        private String base64_cover_image;


        // Getter Methods

        public String getGender() {
            return gender;
        }

        public String getDate_of_birth() {
            return date_of_birth;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getAddress() {
            return address;
        }

        public String getShop_name() {
            return shop_name;
        }

        public String getResponsible_officer() {
            return responsible_officer;
        }

        public String getJob() {
            return job;
        }

        public String getProfile_photo() {
            return profile_photo;
        }

        public String getCover_photo() {
            return cover_photo;
        }

        public String getCreated() {
            return created;
        }

        public String getModified() {
            return modified;
        }

        public float getUser_status() {
            return user_status;
        }

        public float getRecord_status() {
            return record_status;
        }

        public float getProvince() {
            return province;
        }

        public String getMarital_status() {
            return marital_status;
        }

        public String getShop_address() {
            return shop_address;
        }

        public String getWing_account_number() {
            return wing_account_number;
        }

        public String getWing_account_name() {
            return wing_account_name;
        }

        public float getPlace_of_birth() {
            return place_of_birth;
        }

        public String getBase64_profile_image() {
            return base64_profile_image;
        }

        public String getBase64_cover_image() {
            return base64_cover_image;
        }

        // Setter Methods

        public void setGender( String gender ) {
            this.gender = gender;
        }

        public void setDate_of_birth( String date_of_birth ) {
            this.date_of_birth = date_of_birth;
        }

        public void setTelephone( String telephone ) {
            this.telephone = telephone;
        }

        public void setAddress( String address ) {
            this.address = address;
        }

        public void setShop_name( String shop_name ) {
            this.shop_name = shop_name;
        }

        public void setResponsible_officer( String responsible_officer ) {
            this.responsible_officer = responsible_officer;
        }

        public void setJob( String job ) {
            this.job = job;
        }

        public void setProfile_photo( String profile_photo ) {
            this.profile_photo = profile_photo;
        }

        public void setCover_photo( String cover_photo ) {
            this.cover_photo = cover_photo;
        }

        public void setCreated( String created ) {
            this.created = created;
        }

        public void setModified( String modified ) {
            this.modified = modified;
        }

        public void setUser_status( float user_status ) {
            this.user_status = user_status;
        }

        public void setRecord_status( float record_status ) {
            this.record_status = record_status;
        }

        public void setProvince( float province ) {
            this.province = province;
        }

        public void setMarital_status( String marital_status ) {
            this.marital_status = marital_status;
        }

        public void setShop_address( String shop_address ) {
            this.shop_address = shop_address;
        }

        public void setWing_account_number( String wing_account_number ) {
            this.wing_account_number = wing_account_number;
        }

        public void setWing_account_name( String wing_account_name ) {
            this.wing_account_name = wing_account_name;
        }

        public void setPlace_of_birth( float place_of_birth ) {
            this.place_of_birth = place_of_birth;
        }

        public void setBase64_profile_image( String base64_profile_image ) {
            this.base64_profile_image = base64_profile_image;
        }

        public void setBase64_cover_image( String base64_cover_image ) {
            this.base64_cover_image = base64_cover_image;
        }
    }
}

