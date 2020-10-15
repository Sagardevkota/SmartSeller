package com.example.smartseller.util.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences prefs;
    Context context;

    public Session(Context cntx) {
        this.context=cntx;
        // TODO Auto-generated constructor stub
        prefs = cntx.getSharedPreferences("SmartSellerLogin", Context.MODE_PRIVATE);;
    }

    public void setUsername(String username) {
        prefs.edit().putString("userName", username).apply();
    }

    public void setUserId(Integer userId){
        prefs.edit().putInt("userId",userId).apply();

    }

    public String getusername() {
        String usename = prefs.getString("userName","");
        return usename;
    }

    public Integer getUserId(){
        Integer userId=prefs.getInt("userId",0);
        return userId;
    }

    public void setToken(String token){
        prefs.edit().putString("token",token).apply();


    }
    public String getToken(){
        return prefs.getString("token","");
    }

    public void setJWT(String jwt){
        prefs.edit().putString("jwt",jwt).apply();
    }

    public String getJWT(){
        String jwt=prefs.getString("jwt","");
        return jwt;
    }

    public void setBadgeCount(Integer badge_count){
        prefs.edit().putInt("badge_count",badge_count).apply();
    }
    public Integer getBadgeCount(){
        return prefs.getInt("badge_count",0);
    }

    public void destroy() {
        prefs.edit().clear().commit();
    }

}