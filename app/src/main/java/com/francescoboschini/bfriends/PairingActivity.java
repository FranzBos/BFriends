package com.francescoboschini.bfriends;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.francescoboschini.bfriends.BluetoothService.Bluetooth;
import com.francescoboschini.bfriends.BluetoothService.PairingService;
import com.francescoboschini.bfriends.BluetoothService.PairingServiceCallback;

public class PairingActivity extends AppCompatActivity implements PairingServiceCallback {

    private BluetoothAdapter bluetoothAdapter;
    private PairingService service;
    public static final String DEVICE_TO_BE_PAIRED = "device_to_be_paired";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        BluetoothDevice device = getIntent().getParcelableExtra(DEVICE_TO_BE_PAIRED);

        bluetoothAdapter = new Bluetooth(this).getBluetoothAdapter();
        service = new PairingService(this);

        registerReceiver(service, getPairingIntentFilter());

        service.pairDevice(device);
    }

    @NonNull
    private IntentFilter getPairingIntentFilter() {
        return new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        showToast("Paired");
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", device);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDeviceUnpaired() {

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
}
