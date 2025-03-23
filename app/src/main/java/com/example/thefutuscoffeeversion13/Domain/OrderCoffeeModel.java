package com.example.thefutuscoffeeversion13.Domain;

public class OrderCoffeeModel {

    private String title;
    private String picture;
    private String price;

    public OrderCoffeeModel (String title, String picture, String price) {
        this.title = title;
        this.picture = picture;
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
