package com.francescoboschini.bfriends.BluetoothService;

import android.bluetooth.BluetoothDevice;

public interface DiscoveryServiceCallback {

    void onDiscoveryStarted();
    void onDeviceFound(BluetoothDevice device);
    void onDiscoveryFinished();
}
