package com.francescoboschini.bfriends.BluetoothService;

import android.bluetooth.BluetoothDevice;

public interface PairingServiceCallback {
    void onDevicePaired(BluetoothDevice device);
    void onDeviceUnpaired();
}