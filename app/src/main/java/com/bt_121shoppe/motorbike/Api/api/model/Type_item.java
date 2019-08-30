package com.bt_121shoppe.motorbike.Api.api.model;

public class Type_item {
        private float id;
        private float sale_status;
        private float record_status;
        private String sold_date = null;
        private String price;
        private String total_price;

        // Getter Methods

        public float getId() {
            return id;
        }

        public float getSale_status() {
            return sale_status;
        }

        public float getRecord_status() {
            return record_status;
        }

        public String getSold_date() {
            return sold_date;
        }

        public String getPrice() {
            return price;
        }

        public String getTotal_price() {
            return total_price;
        }

        // Setter Methods

        public void setId( float id ) {
            this.id = id;
        }

        public void setSale_status( float sale_status ) {
            this.sale_status = sale_status;
        }

        public void setRecord_status( float record_status ) {
            this.record_status = record_status;
        }

        public void setSold_date( String sold_date ) {
            this.sold_date = sold_date;
        }

        public void setPrice( String price ) {
            this.price = price;
        }

        public void setTotal_price( String total_price ) {
            this.total_price = total_price;
        }
}
