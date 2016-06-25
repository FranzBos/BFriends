package com.francescoboschini.bfriends.BluetoothService.Discovery;

import android.bluetooth.BluetoothDevice;

public interface DiscoveryServiceCallback {

    void onDiscoveryStarted();
    void onDeviceFound(BluetoothDevice device);
    void onDiscoveryFinished();
}
