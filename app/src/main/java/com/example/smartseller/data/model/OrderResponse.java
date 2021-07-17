package com.example.smartseller.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;


@Data
public class OrderResponse implements Parcelable {


    private int orderId;
    private int productId;
    private String userName;
    private String phone;
    private String productColor;
    private float productSize;
    private int price;
    private int quantity;
    private String orderedDate;
    private String deliveredDate;
    private String deliveryAddress;
    private String status;
    //productsInfo
    private String productName;
    private int discount;
    private String picturePath;


    protected OrderResponse(Parcel in) {
        orderId = in.readInt();
        productId = in.readInt();
        userName = in.readString();
        phone = in.readString();
        productColor = in.readString();
        productSize = in.readFloat();
        price = in.readInt();
        quantity = in.readInt();
        orderedDate = in.readString();
        deliveredDate = in.readString();
        deliveryAddress = in.readString();
        status = in.readString();
        productName = in.readString();
        discount = in.readInt();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeInt(productId);
        dest.writeString(userName);
        dest.writeString(phone);
        dest.writeString(productColor);
        dest.writeFloat(productSize);
        dest.writeInt(price);
        dest.writeInt(quantity);
        dest.writeString(orderedDate);
        dest.writeString(deliveredDate);
        dest.writeString(deliveryAddress);
        dest.writeString(status);
        dest.writeString(productName);
        dest.writeInt(discount);
        dest.writeString(picturePath);
    }
}
