package com.bt_121shoppe.lucky_app.Api.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Breand implements Serializable {

        private String category = null;
        private String brand_name;
        private String brand_name_as_kh;
        private String description;


        // Getter Methods

        public String getCategory() {
            return category;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public String getBrand_name_as_kh() {
            return brand_name_as_kh;
        }

        public String getDescription() {
            return description;
        }

        // Setter Methods

        public void setCategory( String category ) {
            this.category = category;
        }

        public void setBrand_name( String brand_name ) {
            this.brand_name = brand_name;
        }

        public void setBrand_name_as_kh( String brand_name_as_kh ) {
            this.brand_name_as_kh = brand_name_as_kh;
        }

        public void setDescription( String description ) {
            this.description = description;
        }

}

