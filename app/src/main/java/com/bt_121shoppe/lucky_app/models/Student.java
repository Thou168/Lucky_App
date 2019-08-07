package com.bt_121shoppe.lucky_app.models;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID=1L;
    private String name;
    private String emailId;
    public Student(){

    }
    public Student(String name,String emailId){
        this.name=name;
        this.emailId=emailId;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
