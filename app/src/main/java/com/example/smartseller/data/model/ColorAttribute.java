package com.example.smartseller.data.model;

public class ColorAttribute {


    private Integer id;

    private Integer product_id;

    private String color;

    public ColorAttribute(Integer product_id, String color) {
        this.product_id = product_id;
        this.color = color;
    }

    public ColorAttribute() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
