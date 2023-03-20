package com.hardik.vendors;

import java.util.ArrayList;
import java.util.HashMap;

public class Vendor {
    String shopname, name,phone,category,email,password,uid;
    double lat, lang;
    ArrayList<Item> items;

    public Vendor() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Vendor(String shopname, String name, String phone, String category, String email, String password, double lat, double lang, String uid) {
        this.shopname = shopname;
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.email = email;
        this.password = password;
        this.lat = lat;
        this.lang = lang;
        this.uid = uid;
    }

    public Vendor(String shopname, String name, String phone, String category, String password) {
        this.shopname = shopname;
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.password = password;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "shopname='" + shopname + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                ", password='" + password + '\'' +
                ", lat=" + lat +
                ", lang=" + lang +
                '}';
    }
}
