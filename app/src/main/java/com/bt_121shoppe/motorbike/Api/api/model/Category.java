package com.bt_121shoppe.motorbike.Api.api.model;

public class Category extends Breand {
        private String cat_name;
        private String cat_name_kh;
        private String cat_description;
        private String cat_image_path = null;
        private String record_status = null;


        // Getter Methods

        public String getCat_name() {
            return cat_name;
        }

        public String getCat_name_kh() {
            return cat_name_kh;
        }

        public String getCat_description() {
            return cat_description;
        }

        public String getCat_image_path() {
            return cat_image_path;
        }

        public String getRecord_status() {
            return record_status;
        }

        // Setter Methods

        public void setCat_name( String cat_name ) {
            this.cat_name = cat_name;
        }

        public void setCat_name_kh( String cat_name_kh ) {
            this.cat_name_kh = cat_name_kh;
        }

        public void setCat_description( String cat_description ) {
            this.cat_description = cat_description;
        }

        public void setCat_image_path( String cat_image_path ) {
            this.cat_image_path = cat_image_path;
        }

        public void setRecord_status( String record_status ) {
            this.record_status = record_status;
        }
}
