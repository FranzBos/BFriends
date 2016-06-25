package com.francescoboschini.bfriends.BluetoothService.PairingService;

import android.bluetooth.BluetoothDevice;

public interface PairingServiceCallback {
    void onDevicePaired(BluetoothDevice device);
}