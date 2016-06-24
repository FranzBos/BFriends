package com.francescoboschini.bfriends;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FriendDevice extends RealmObject {
    private String name;
    @PrimaryKey
    private String macAddress;

    public FriendDevice() {
    }

    public FriendDevice(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}