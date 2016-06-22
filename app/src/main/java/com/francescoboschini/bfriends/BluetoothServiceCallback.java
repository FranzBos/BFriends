package com.francescoboschini.bfriends;

import android.bluetooth.BluetoothDevice;

public interface BluetoothServiceCallback {

    void onDiscoveryStarted();
    void onDeviceFound(BluetoothDevice device);
    void onDiscoveryFinished();
    void onDevicePaired();
    void onDeviceUnpaired();
}
