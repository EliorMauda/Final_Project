package com.example.finalproject.Domain;

public class SliderItems {
    private String url;
    private int discount;
    String code;

    public SliderItems() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SliderItems(String url) {
        this.url = url;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
