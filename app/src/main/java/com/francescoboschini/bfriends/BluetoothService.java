package com.francescoboschini.bfriends;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Method;

public class BluetoothService extends BroadcastReceiver {

    public static final String CREATE_BOND_METHOD = "createBond";
    public static final String REMOVE_BOND_METHOD = "removeBond";
    BluetoothServiceCallback callback;

    public BluetoothService(BluetoothServiceCallback callback) {
        this.callback = callback;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            callback.onDiscoveryStarted();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            callback.onDiscoveryFinished();
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            callback.onDeviceFound(device);
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            if (isPaired(state, prevState)) {
                callback.onDevicePaired();
            } else if (isUnpaired(state, prevState)) {
                callback.onDeviceUnpaired();
            }
        }
    }

    private boolean isUnpaired(int state, int prevState) {
        return state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED;
    }

    private boolean isPaired(int state, int prevState) {
        return state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING;
    }

    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod(CREATE_BOND_METHOD, (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod(REMOVE_BOND_METHOD, (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
