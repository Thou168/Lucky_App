package com.bt_121shoppe.motorbike.models;

public class NotificationSenderViewModel {

    public NotificationDataViewModel data;
    public String To;

    public NotificationSenderViewModel(NotificationDataViewModel data,String To){
        this.data=data;
        this.To=To;
    }

}
