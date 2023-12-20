package com.example.thefutuscoffeeversion13.Domain;

public class CardModel {
    private String img_url;
    private String title;
    private String price;
    private String description;
    private String quantity;
    private String size;
    private String topping;
    private String totalprice;

    public CardModel() {
    }

    public CardModel(String img_url, String title, String price, String description, String quantity, String size, String topping, String totalprice) {
        this.img_url = img_url;
        this.title = title;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.size = size;
        this.topping = topping;
        this.totalprice = totalprice;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalprice;
    }

    public void setTotalPrice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }
}
