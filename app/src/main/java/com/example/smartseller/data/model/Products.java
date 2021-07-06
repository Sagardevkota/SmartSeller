package com.example.smartseller.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products implements Parcelable {

    private int productId;
    private String productName;
    private String desc;
    private String price;
    private String category;
    private String brand;
    private String sku;
    private String type;
    private String picturePath;
    private Integer discount;
    private Integer stock;
    private Integer seller_id;
    private String rating;
    private List<String> colors;
    private List<String> sizes;


    protected Products(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        desc = in.readString();
        price = in.readString();
        category = in.readString();
        brand = in.readString();
        sku = in.readString();
        type = in.readString();
        picturePath = in.readString();
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
        rating = in.readString();
        colors = in.createStringArrayList();
        sizes = in.createStringArrayList();
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

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(picturePath);
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
        dest.writeString(rating);
        dest.writeStringList(colors);
        dest.writeStringList(sizes);
    }
}
