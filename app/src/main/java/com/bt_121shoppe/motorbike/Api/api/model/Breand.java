package com.bt_121shoppe.motorbike.Api.api.model;


public class Breand extends Item{

        private String brand_name;
        private String brand_name_as_kh;


        // Getter Methods


        public String getBrand_name() {
            return brand_name;
        }

        public String getBrand_name_as_kh() {
            return brand_name_as_kh;
        }


        // Setter Methods


        public void setBrand_name( String brand_name ) {
            this.brand_name = brand_name;
        }

        public void setBrand_name_as_kh( String brand_name_as_kh ) {
            this.brand_name_as_kh = brand_name_as_kh;
        }

}

