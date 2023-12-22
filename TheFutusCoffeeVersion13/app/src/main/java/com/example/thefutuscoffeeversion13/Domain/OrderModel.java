package com.example.thefutuscoffeeversion13.Domain;

public class OrderModel {
    private String name, idOrder, daytime, day, time, status, price, phoneNumber, address;
    public OrderModel() {
    }

    public OrderModel(String name, String idOrder, String daytime, String day, String time, String status, String price, String phoneNumber, String address) {
        this.name = name;
        this.idOrder = idOrder;
        this.daytime = daytime;
        this.day = day;
        this.time = time;
        this.status = status;
        this.price = price;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
