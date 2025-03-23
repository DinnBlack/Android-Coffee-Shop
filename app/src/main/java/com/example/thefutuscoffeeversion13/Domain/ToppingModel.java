package com.example.thefutuscoffeeversion13.Domain;

public class ToppingModel {
    private String title;
    private String price;
    private boolean isSelected;

    public ToppingModel(String title, String price) {
        this.title = title;
        this.price = price;
        this.isSelected = false;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
