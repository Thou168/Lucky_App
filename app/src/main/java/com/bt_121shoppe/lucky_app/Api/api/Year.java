package com.bt_121shoppe.lucky_app.Api.api;

public class Year extends Category{
        private String year;
        private String modified = null;

        // Getter Methods

        public String getYear() {
            return year;
        }

        public String getModified() {
            return modified;
        }

        // Setter Methods

        public void setYear( String year ) {
            this.year = year;
        }

        public void setModified( String modified ) {
            this.modified = modified;
        }
}
