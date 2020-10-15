package com.example.smartseller.data.model;


public class SizeAttribute {


    private Integer id;

    private Integer product_id;

    private String size;

    public SizeAttribute(Integer product_id, String size) {
        this.product_id = product_id;
        this.size = size;
    }

    public SizeAttribute() {
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
