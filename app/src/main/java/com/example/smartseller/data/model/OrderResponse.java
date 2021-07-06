package com.example.smartseller.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;


@Data
public class OrderResponse implements Parcelable {


    private Integer orderId;

    private Integer productId;

    private String userName;

    private String phone;


    private String color;

    private Float size;

    private Integer price;

    private Integer quantity;

    private String orderedDate;

    private String deliveredDate;

    private String deliveryAddress;

    private String status;

    //productsInfo
    private String productName;
    private Integer discount;
    private String picturePath;


    protected OrderResponse(Parcel in) {
        if (in.readByte() == 0) {
            orderId = null;
        } else {
            orderId = in.readInt();
        }
        if (in.readByte() == 0) {
            productId = null;
        } else {
            productId = in.readInt();
        }
        userName = in.readString();
        phone = in.readString();
        color = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readFloat();
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
        orderedDate = in.readString();
        deliveredDate = in.readString();
        deliveryAddress = in.readString();
        status = in.readString();
        productName = in.readString();
        if (in.readByte() == 0) {
            discount = null;
        } else {
            discount = in.readInt();
        }
        picturePath = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (orderId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(orderId);
        }
        if (productId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(productId);
        }
        parcel.writeString(userName);
        parcel.writeString(phone);
        parcel.writeString(color);
        if (size == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(size);
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
        parcel.writeString(orderedDate);
        parcel.writeString(deliveredDate);
        parcel.writeString(deliveryAddress);
        parcel.writeString(status);
        parcel.writeString(productName);
        if (discount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(discount);
        }
        parcel.writeString(picturePath);
    }
}
