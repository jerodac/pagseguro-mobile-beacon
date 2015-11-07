package br.com.uol.ps.beacon.others;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.uol.ps.beacon.business.DataStorageApp;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class DeviceFoundBuffer {
    private List<DeviceFound> listDevices;
    private Context mContext;

    public DeviceFoundBuffer(Context context) {
        this.mContext = context;
        List<DeviceFound> deviceFounds = retrieve();
        if (deviceFounds == null) {
            listDevices = deviceFounds;
        } else {
            listDevices = new ArrayList<DeviceFound>();
        }
    }

    private List<DeviceFound> retrieve() {
        return DataStorageApp.retrieveDeviceFoundBuffer(mContext);
    }

    public List<DeviceFound> getListDeviceFound() {
        return listDevices;
    }

    public void add(DeviceFound deviceFound) {
        listDevices.add(deviceFound);
        DataStorageApp.saveFoundDeviceBuffer(mContext, this);
    }

    public void clearPersistence() {
        DataStorageApp.clearPersistenceDeviceBuffer(mContext);
    }

}
