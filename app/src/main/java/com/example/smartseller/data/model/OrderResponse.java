package com.example.smartseller.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class OrderResponse implements Parcelable {


    private Integer order_id;

    private Integer product_id;

    private String user_name;

    private String phone;


    private String product_color;

    private Float product_size;

    private Integer price;

    private Integer quantity;

    private String ordered_date;

    private String delivered_date;

    private String delivery_address;

    private String status;

    //productsInfo
    private String product_name;
    private Integer discount;
    private String picture_path;



    //for user
    public OrderResponse(Integer order_id, Integer product_id, String product_color, Float product_size, Integer price, Integer quantity, String ordered_date, String delivered_date, String delivery_address, String status, String product_name, Integer discount, String picture_path) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.product_color = product_color;
        this.product_size = product_size;
        this.price = price;
        this.quantity = quantity;
        this.ordered_date = ordered_date;
        this.delivered_date = delivered_date;
        this.delivery_address = delivery_address;
        this.status = status;
        this.product_name = product_name;
        this.discount = discount;
        this.picture_path = picture_path;
    }

    //for seller

    public OrderResponse(Integer order_id, String user_name,String phone, Integer product_id, String product_color, Float product_size, Integer price, Integer quantity, String ordered_date, String delivered_date, String delivery_address, String status, String product_name, Integer discount, String picture_path) {
        this.order_id = order_id;
        this.user_name=user_name;
        this.phone=phone;
        this.product_id = product_id;
        this.product_color = product_color;
        this.product_size = product_size;
        this.price = price;
        this.quantity = quantity;
        this.ordered_date = ordered_date;
        this.delivered_date = delivered_date;
        this.delivery_address = delivery_address;
        this.status = status;
        this.product_name = product_name;
        this.discount = discount;
        this.picture_path = picture_path;
    }

    public OrderResponse(OrderResponse o) {
        this.order_id = o.getOrder_id();
        this.user_name=o.getUser_name();
        this.phone=o.getPhone();
        this.product_id = o.getProduct_id();
        this.product_color = o.getProduct_color();
        this.product_size = o.getProduct_size();
        this.price = o.getPrice();
        this.quantity = o.getQuantity();
        this.ordered_date = o.getOrdered_date();
        this.delivered_date = o.getDelivered_date();
        this.delivery_address = o.getDelivery_address();
        this.status = o.getStatus();
        this.product_name = o.getProduct_name();
        this.discount = o.getDiscount();
        this.picture_path = o.getPicture_path();
    }




    protected OrderResponse(Parcel in) {
        if (in.readByte() == 0) {
            order_id = null;
        } else {
            order_id = in.readInt();
        }
        if (in.readByte() == 0) {
            product_id = null;
        } else {
            product_id = in.readInt();
        }
        user_name = in.readString();
        phone = in.readString();
        product_color = in.readString();
        if (in.readByte() == 0) {
            product_size = null;
        } else {
            product_size = in.readFloat();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        ordered_date = in.readString();
        delivered_date = in.readString();
        delivery_address = in.readString();
        status = in.readString();
        product_name = in.readString();
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readInt();
        }
        picture_path = in.readString();
    }

    public static final Creator<OrderResponse> CREATOR = new Creator<OrderResponse>() {
        @Override
        public OrderResponse createFromParcel(Parcel in) {
            return new OrderResponse(in);
        }

        @Override
        public OrderResponse[] newArray(int size) {
            return new OrderResponse[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public Float getProduct_size() {
        return product_size;
    }

    public void setProduct_size(Float product_size) {
        this.product_size = product_size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (order_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(order_id);
        }
        if (product_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(product_id);
        }
        parcel.writeString(user_name);
        parcel.writeString(phone);
        parcel.writeString(product_color);
        if (product_size == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(product_size);
        }
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(price);
        }
        if (quantity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(quantity);
        }
        parcel.writeString(ordered_date);
        parcel.writeString(delivered_date);
        parcel.writeString(delivery_address);
        parcel.writeString(status);
        parcel.writeString(product_name);
        if (discount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(discount);
        }
        parcel.writeString(picture_path);
    }
}
