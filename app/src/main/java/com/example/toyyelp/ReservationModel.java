package com.example.toyyelp;

public class ReservationModel {
    int orderNum;
    String name;
    String date;
    String time;
    String email;
    String id;


    public ReservationModel(int orderNum, String name, String date, String time, String email, String id) {
        this.orderNum = orderNum;
        this.name = name;
        this.date = date;
        this.time = time;
        this.email = email;
        this.id = id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
