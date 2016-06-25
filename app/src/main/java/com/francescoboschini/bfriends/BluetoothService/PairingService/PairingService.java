package com.francescoboschini.bfriends.BluetoothService.PairingService;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class PairingService extends BroadcastReceiver {

    PairingServiceCallback callback;
    public static final String CREATE_BOND_METHOD = "createBond";
    public static final String REMOVE_BOND_METHOD = "removeBond";

    public PairingService(PairingServiceCallback callback) {
        this.callback = callback;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            if (isPaired(state, prevState)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                callback.onDevicePaired(device);
                Log.d("PAIRED", "Paired : " + device.getAddress());
            }
        }
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

    private boolean isPaired(int state, int prevState) {
        return state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING;
    }

    private boolean isUnpaired(int state, int prevState) {
        return state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED;
    }
}
