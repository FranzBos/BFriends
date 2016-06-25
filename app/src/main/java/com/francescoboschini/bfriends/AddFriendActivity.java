package com.francescoboschini.bfriends;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.francescoboschini.bfriends.BluetoothService.Bluetooth;
import com.francescoboschini.bfriends.BluetoothService.Discovery.DiscoveringService;
import com.francescoboschini.bfriends.BluetoothService.Discovery.DiscoveryServiceCallback;
import com.francescoboschini.bfriends.Realm.RealmDatabase;

import java.util.ArrayList;

import io.realm.Realm;

public class AddFriendActivity extends AppCompatActivity implements DiscoveryServiceCallback, OnPairButtonClickListener {

    public static final int REQUEST_CODE = 87;
    public static final String DEVICE_TO_BE_PAIRED = "device_to_be_paired";
    public static final String DEVICE_PAIRED_RESULT = "device_paired_result";
    private DeviceListAdapter listAdapter;
    private ArrayList<BluetoothDevice> devicesList;
    private BluetoothAdapter bluetoothAdapter;
    private DiscoveringService service;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bluetoothAdapter = new Bluetooth(this).getBluetoothAdapter();
        service = new DiscoveringService(this);

        realm = new RealmDatabase(this).getInstance();

        devicesList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.lv_paired);
        listAdapter = new DeviceListAdapter(this);
        listAdapter.setData(devicesList);
        listAdapter.setListener(this);
        mListView.setAdapter(listAdapter);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                BluetoothDevice result = data.getParcelableExtra(DEVICE_PAIRED_RESULT);
                showToast("RESULT " + result.getAddress());
                listAdapter.notifyDataSetChanged();
                saveDeviceToDatabase(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
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

    @Override
    public void onDestroy() {
        unregisterReceiver(service);
        super.onDestroy();
    }

    @Override
    public void onDiscoveryStarted() {
        showToast("DISCOVERY STARTED");
        devicesList.clear();
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        devicesList.add(device);
        listAdapter.notifyDataSetChanged();
        listAdapter.setData(devicesList);
        showToast("Found device: " + device.getName());
    }

    @Override
    public void onDiscoveryFinished() {
        showToast("DISCOVERY FINISHED");
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

    @Override
    public void onPairButtonClick(int position) {
        BluetoothDevice device = devicesList.get(position);
        Intent intent = new Intent(this, PairingActivity.class);
        intent.putExtra(DEVICE_TO_BE_PAIRED, device);
        startActivityForResult(intent, REQUEST_CODE);

    }

    public void restartDiscovering(View view) {
        bluetoothAdapter.startDiscovery();
    }

    public void saveDeviceToDatabase(BluetoothDevice device) {
        listAdapter.notifyDataSetChanged();
        realm.beginTransaction();
        FriendDevice friendDevice = new FriendDevice(device.getName(), device.getAddress());
        realm.copyToRealmOrUpdate(friendDevice);
        realm.commitTransaction();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
