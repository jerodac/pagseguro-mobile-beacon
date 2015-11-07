package br.com.uol.ps.beacon.components;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.uol.ps.beacon.others.DeviceFound;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * @author Dario Calzoli 28/10/15.
 * @author Jean Rodrigo D. CUnha - Refactor classe 30/10/15
 */
public class BeaconBluetooth extends BroadcastReceiver {

    private BluetoothListener mBluetoothListener;
    private BluetoothAdapter mBluetoothAdapter;
    private List<DeviceFound> listDeviceFound;
    private Context mContext;

    public BeaconBluetooth(Context context, BluetoothListener mBluetoothListener) {
        this.mBluetoothListener = mBluetoothListener;
        listDeviceFound = new ArrayList<DeviceFound>();
        mContext = context;
    }

    //inicia a busca de dispositivos
    private void startFindDevices() throws IOException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothListener.ACTION_FOUND);
        IntentFilter filter2 = new IntentFilter(BluetoothListener.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(BluetoothListener.ACTION_DISCOVERY_STARTED);
        mContext.registerReceiver(this, filter);
        mContext.registerReceiver(this, filter2);
        mContext.registerReceiver(this, filter3);

        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Adapter para buscar a densidade do sinal
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        String action = intent.getAction();
        BTAdapter.startDiscovery();
        ApplicationUtilities.log(Log.INFO, "onReceive");
        if (action.compareTo(BluetoothDevice.ACTION_FOUND) == 0) {
            ApplicationUtilities.log(Log.INFO, "BluetoothDevice.ACTION_FOUND");
            //Entra aqui quando encontra um dispositivo

            //propriedade do dispositivo
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            //sinal do dispositivo
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            listDeviceFound.add(new DeviceFound(device.getAddress(), rssi));

        } else if (action.compareTo(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) == 0) {
            ApplicationUtilities.log(Log.INFO, "BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
            //entra aqui quando finaliza a busca
            context.unregisterReceiver(this);
            setBluetooth(false);
        }

        if (mBluetoothListener != null) {
            mBluetoothListener.action(action, listDeviceFound);
        }
    }

    public void searchBeacon() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setBluetooth(true);
                try {
                    Thread.sleep(1000);
                    startFindDevices();
                } catch (Exception e) {
                    ApplicationUtilities.log(Log.ERROR, getClass().getSimpleName(), e);
                }
            }
        }).start();
    }

    private boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }

    public static interface BluetoothListener {
        public static final String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
        public static final String ACTION_FOUND = BluetoothDevice.ACTION_FOUND;
        public static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;

        public void action(String action, List<DeviceFound> devicesFounds);
    }

}
