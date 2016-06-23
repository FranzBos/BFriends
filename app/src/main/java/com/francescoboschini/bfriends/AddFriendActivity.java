package com.francescoboschini.bfriends;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AddFriendActivity extends AppCompatActivity implements BluetoothServiceCallback, OnPairButtonClickListener {

    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService service;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        service = new BluetoothService(this);

        realm = new RealmDatabase(this).getInstance();

        mDeviceList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.lv_paired);
        mAdapter = new DeviceListAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);

        RealmResults<FriendDevice> friends = realm.where(FriendDevice.class).findAll();

        registerReceiver(service, getDiscoveryIntentFilter());
        registerReceiver(service, getPairingIntentFilter());
    }

    @NonNull
    private IntentFilter getPairingIntentFilter() {
        return new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    }

    @NonNull
    private IntentFilter getDiscoveryIntentFilter() {
        IntentFilter discoveryFilter = new IntentFilter();
        discoveryFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        return discoveryFilter;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(service);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDiscoveryStarted() {
        showToast("DISCOVERY STARTED");
        mDeviceList.clear();
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        mDeviceList.add(device);
        mAdapter.notifyDataSetChanged();
        mAdapter.setData(mDeviceList);
        showToast("Found device " + device.getName());
    }

    @Override
    public void onDiscoveryFinished() {
        showToast("DISCOVERY FINISHED");
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        showToast("Paired");
        mAdapter.notifyDataSetChanged();
        realm.beginTransaction();
        FriendDevice friendDevice = new FriendDevice(device.getName(), device.getAddress());
        realm.copyToRealm(friendDevice);
        realm.commitTransaction();
    }

    @Override
    public void onDeviceUnpaired() {
        showToast("Unpaired");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPairButtonClick(int position) {
        BluetoothDevice device = mDeviceList.get(position);
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            service.unpairDevice(device);
        } else {
            showToast("Pairing...");
            service.pairDevice(device);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void restartDiscovering(View view) {
        bluetoothAdapter.startDiscovery();
    }
}
