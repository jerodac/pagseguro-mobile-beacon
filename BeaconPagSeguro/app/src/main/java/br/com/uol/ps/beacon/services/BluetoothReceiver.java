package br.com.uol.ps.beacon.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import br.com.uol.ps.beacon.BeaconPagSeguroApp;
import br.com.uol.ps.beacon.business.BetterAsyncTask;
import br.com.uol.ps.beacon.others.DeviceFound;
import br.com.uol.ps.beacon.others.DeviceFoundBuffer;
import br.com.uol.ps.beacon.others.OffersFacade;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;
import br.com.uol.ps.beacon.vo.BaseResponseVO;
import br.com.uol.ps.beacon.vo.BeaconRequestVO;
import br.com.uol.ps.beacon.vo.BeaconResponseVO;
import retrofit.RetrofitError;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class BluetoothReceiver extends BroadcastReceiver {

    private DeviceFoundBuffer deviceFoundBuffer;

    @Override
    public void onReceive(Context context, Intent intent) {
        deviceFoundBuffer = new DeviceFoundBuffer(context);
        String action = intent.getAction();

        if (action.compareTo(BluetoothDevice.ACTION_FOUND) == 0) {
            ApplicationUtilities.log(Log.INFO, "BluetoothDevice.ACTION_FOUND");
            //Entra aqui quando encontra um dispositivo

            //propriedade do dispositivo
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            //sinal do dispositivo
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            //listDeviceFound.add(new DeviceFound(device.getAddress(), rssi));
            deviceFoundBuffer.add(new DeviceFound(device.getAddress(), rssi));

        } else if (action.compareTo(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) == 0) {
            ApplicationUtilities.log(Log.INFO, "BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
            //context.unregisterReceiver(this);
            //desativa bluetooth
            List<DeviceFound> bufferDevices = deviceFoundBuffer.getListDeviceFound();
            if (bufferDevices.size() > 0) {
                queryPromoBeacons(context, bufferDevices.get(0));
            } else {
                ApplicationUtilities.log(Log.INFO, "DevicesFounds == 0 | Sem dispositivos bluetooth nas proximidades");
            }
            deviceFoundBuffer.clearPersistence();
            //TODO Desligar bluetooth

        }

    }

    private void queryPromoBeacons(final Context context, final DeviceFound deviceFound) {
        final OffersFacade offersFacade = new OffersFacade(context);
        new BetterAsyncTask<BaseResponseVO>() {
            @Override
            protected BaseResponseVO doIt() {
                return BeaconPagSeguroApp.getApi().beacons(new BeaconRequestVO(deviceFound.getMac()));
            }

            @Override
            protected void onResult(BaseResponseVO result) {
                ApplicationUtilities.log(Log.INFO, "OnResult");
                final BeaconResponseVO beaconResponseVO = (BeaconResponseVO) result;
                ApplicationUtilities.log(Log.INFO, "Result: " + beaconResponseVO.toString());

                offersFacade.add(beaconResponseVO.getOffers());
                offersFacade.save(context);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        NotificationService.callNotification(context,
                                "Bateu fome? O carrinho chegou... \n" +
                                        "Oferta do dia: " + beaconResponseVO.getOffers().get(0).getTitle());

                    }
                });
            }

            @Override
            protected void onError(Exception ex) {
                ApplicationUtilities.log(Log.INFO, "OnError");
                ApplicationUtilities.log(Log.ERROR, "exception: " + ex.getMessage());
                BaseResponseVO response = null;
                try {
                    response = (BaseResponseVO) ((RetrofitError) ex)
                            .getBodyAs(BaseResponseVO.class);
                } catch (Exception e) {
                    ApplicationUtilities.log(Log.ERROR, e.getMessage());
                }
                if (response != null) {
                    ApplicationUtilities.log(Log.ERROR, "ErrorCode: " + response.getErrorCode() + "Message: " + response.getMessage());
                } else {
                    //network error
                    ApplicationUtilities.log(Log.ERROR, "network error");
                }
                Toast.makeText(context, "Falha ao buscar ofertas", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
