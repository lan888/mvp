package com.example.ian.mvp.mvp.model;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ian on 2017/8/28 0028.
 */

public class Rooms extends BmobObject {
    private String status;
    private String type;
    private String area;
    private String addressInfo;
    private String addressDetail;
    private String price;
    private String contacts;
    private String floor;
    private String contactsTel;
    private String direction;
    private String allFloor;
    private String houseDes;
    private String user;
    private String tenant;
    private ArrayList<String> houseImg;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<String> getHouseImg() {
        return houseImg;
    }

    public void setHouseImg(ArrayList<String> houseImg) {
        this.houseImg = houseImg;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAllFloor() {
        return allFloor;
    }

    public void setAllFloor(String allFloor) {
        this.allFloor = allFloor;
    }

    public String getHouseDes() {
        return houseDes;
    }

    public void setHouseDes(String houseDes) {
        this.houseDes = houseDes;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getContactsTel() {
        return contactsTel;
    }

    public void setContactsTel(String contactsTel) {
        this.contactsTel = contactsTel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

