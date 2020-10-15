package com.example.smartseller.data.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Products implements  Parcelable {


    private int productId;

    private String productName;

    private String desc;


    private String price;

    private String category;

    private String brand;

    private String sku;

    private String type;

    private String picture_path;


    private Integer discount;


    private Integer stock;


    private Integer seller_id;

    private String color;
    private String size;

    public Products() {
    }

    //for get
    public Products(int productId, String productName, String desc, String price, String category, String brand, String sku, String type, String picture_path, Integer discount, Integer stock,Integer seller_id) {
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
       this.price=price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
        this.type = type;
        this.picture_path = picture_path;
        this.discount = discount;
        this.stock = stock;
        this.seller_id=seller_id;
    }

    //for passing to another activity
    public Products(int productId, String productName, String desc, String price, String category, String brand, String sku, String type, String picture_path, Integer discount, Integer stock,Integer seller_id,String color,String size) {
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
        this.price=price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
        this.type = type;
        this.picture_path = picture_path;
        this.discount = discount;
        this.stock = stock;
        this.seller_id=seller_id;
        this.color=color;
        this.size=size;
    }

    //for post
    public Products( String productName, String desc, String price, String category, String brand, String sku, String type, String picture_path, Integer discount, Integer stock,Integer seller_id) { this.productId = productId;
        this.productName = productName;
        this.desc = desc;
        this.price=price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
        this.type = type;
        this.picture_path = picture_path;
        this.discount = discount;
        this.stock = stock;
        this.seller_id=seller_id;
    }

    public Products(Products p){
        this.productId = p.getProductId();
        this.productName = p.getProductName();
        this.desc = p.getDesc();
        this.price=p.getPrice();
        this.category = p.getCategory();
        this.brand = p.getBrand();
        this.sku = p.getSku();
        this.type = p.getType();
        this.picture_path = p.getPicture_path();
        this.discount = p.getDiscount();
        this.stock = p.getStock();
        this.seller_id=p.getSeller_id();

    }


    protected Products(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        desc = in.readString();
        price = in.readString();
        category = in.readString();
        brand = in.readString();
        sku = in.readString();
        type = in.readString();
        picture_path = in.readString();
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readInt();
        }
        if (in.readByte() == 0) {
            stock = null;
        } else {
            stock = in.readInt();
        }
        if (in.readByte() == 0) {
            seller_id = null;
        } else {
            seller_id = in.readInt();
        }
        color = in.readString();
        size = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(productName);
        dest.writeString(desc);
        dest.writeString(price);
        dest.writeString(category);
        dest.writeString(brand);
        dest.writeString(sku);
        dest.writeString(type);
        dest.writeString(picture_path);
        if (discount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(discount);
        }
        if (stock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stock);
        }
        if (seller_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(seller_id);
        }
        dest.writeString(color);
        dest.writeString(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", desc='" + desc + '\'' +

                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }



}
