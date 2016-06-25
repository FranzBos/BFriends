package com.francescoboschini.bfriends.Realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDatabase {

    public RealmDatabase(Context context) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public Realm getInstance() {
        return Realm.getDefaultInstance();
    }
}
