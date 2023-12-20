package com.example.thefutuscoffeeversion13.Domain;

public class CardVoucherModel {

    private String img_url;
    private String title;
    private String condition;
    private String expiry;

    public CardVoucherModel() {
        // Default constructor required for Firebase Firestore
    }

    public CardVoucherModel(String img_url, String title, String condition, String expiry) {
        this.img_url = img_url;
        this.title = title;
        this.condition = condition;
        this.expiry = expiry;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

}
