package br.com.uol.ps.beacon.others;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by jeanrodrigo on 06/11/15.
 */
public class SettingsBluetooth {

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }
}
