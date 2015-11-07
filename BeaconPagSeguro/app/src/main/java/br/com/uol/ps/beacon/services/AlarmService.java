package br.com.uol.ps.beacon.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import br.com.uol.ps.beacon.components.BeaconBluetooth;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * Created by dario on 03/11/15.
 *
 * @refactor Jean Rodrigo Dalbon Cunha
 */
public class AlarmService extends Service {

    private BeaconBluetooth beaconBluetooth;
    private BeaconBluetooth.BluetoothListener bluetoothListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startFindDevices();
        NotificationService.callNotification(getApplicationContext(), "isso é um teste");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
    }


    private void startFindDevices() {
        try {
            if (beaconBluetooth == null) {
                beaconBluetooth = new BeaconBluetooth(this, bluetoothListener);
                bluetoothListener = bluetoothListener();
            }
            beaconBluetooth.searchBeacon();
        } catch (Exception e) {
            ApplicationUtilities.log(Log.ERROR, "Beacon PagSeguro", e);
        }
    }

    public BeaconBluetooth.BluetoothListener bluetoothListener() {
        return new BeaconBluetooth.BluetoothListener() {
            @Override
            public void action(String action, final List devicesFounds) {
                if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_STARTED) == 0) {
                    ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_STARTED");
                } else if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_FINISHED) == 0) {
                    ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_FINISHED");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            NotificationService.callNotification(getApplicationContext(),
                                    "Bateu fome? O carrinho chegou... \n" +
                                            "Oferta do dia: Polo loco Apenas R$ 1,00", devicesFounds);
                            ApplicationUtilities.log(Log.INFO, "Bluetooth MACS: " + devicesFounds.toString());
                        }
                    });
                }
            }
        };
    }


}
