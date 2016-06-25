package com.francescoboschini.bfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.francescoboschini.bfriends.Realm.RealmDatabase;

import io.realm.Realm;
import io.realm.RealmResults;

public class FriendsActivity extends AppCompatActivity {

    private FriendsDevicesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Realm realm = new RealmDatabase(this).getInstance();

        ListView mListView = (ListView) findViewById(R.id.friends_list_view);
        adapter = new FriendsDevicesListAdapter(this);

        RealmResults<FriendDevice> devices = realm.where(FriendDevice.class).findAll();
        Log.d("DEVICES ", String.valueOf(devices));
        adapter.setData(devices);

        mListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
