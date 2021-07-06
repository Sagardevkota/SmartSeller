package com.example.smartseller.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Parcelable {
    private int id;
    private String userName;
    private String password;
    private String deliveryAddress;
    private String role;
    private String phone;
    private String gender;
    private String age;

    protected User(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        password = in.readString();
        deliveryAddress = in.readString();
        role = in.readString();
        phone = in.readString();
        gender = in.readString();
        age = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(deliveryAddress);
        parcel.writeString(role);
        parcel.writeString(phone);
        parcel.writeString(gender);
        parcel.writeString(age);
    }
}
