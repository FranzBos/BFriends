package com.francescoboschini.bfriends.BluetoothService;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

public class Bluetooth {

    Context context;

    public Bluetooth(Context context) {
        this.context = context;
    }

    public android.bluetooth.BluetoothAdapter getBluetoothAdapter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            return manager.getAdapter();
        } else {
            return android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        }
    }
}
