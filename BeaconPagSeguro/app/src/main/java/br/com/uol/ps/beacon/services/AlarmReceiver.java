package br.com.uol.ps.beacon.services;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import br.com.uol.ps.beacon.BeaconPagSeguroApp;
import br.com.uol.ps.beacon.business.BetterAsyncTask;
import br.com.uol.ps.beacon.business.DataStorageApp;
import br.com.uol.ps.beacon.components.BeaconBluetooth;
import br.com.uol.ps.beacon.others.OffersFacade;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;
import br.com.uol.ps.beacon.vo.BaseResponseVO;
import br.com.uol.ps.beacon.vo.BeaconRequestVO;
import br.com.uol.ps.beacon.vo.BeaconResponseVO;
import retrofit.RetrofitError;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class AlarmReceiver extends BroadcastReceiver {

    private BeaconBluetooth beaconBluetooth;
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationUtilities.log(Log.VERBOSE, "Receiver Alarm calling!");

        mContext = context;
        //startFindDevices();
        // queryPromoBeacons(mContext);
        DataStorageApp.clearPersistenceDeviceBuffer(context);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        mBluetoothAdapter.startDiscovery();
    }


    BeaconBluetooth.BluetoothListener bluetoothListener = new BeaconBluetooth.BluetoothListener() {
        @Override
        public void action(String action, final List<BeaconBluetooth.DeviceFound> devicesFounds) {
            if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_STARTED) == 0) {
                ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_STARTED");
            } else if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_FINISHED) == 0) {
                ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_FINISHED");
                ApplicationUtilities.log(Log.INFO, "Bluetooth MACS: " + devicesFounds.toString());
                if (devicesFounds.size() > 0) {
                    queryPromoBeacons(mContext, devicesFounds.get(0));
                } else {
                    ApplicationUtilities.log(Log.INFO, "DevicesFounds == 0 | Sem dispositivos bluetooth nas proximidades");
                }
            }
        }
    };


    private void queryPromoBeacons(final Context context, final BeaconBluetooth.DeviceFound deviceFound) {
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

    private void queryPromoBeacons(final Context context) {
        final OffersFacade offersFacade = new OffersFacade(context);
        new BetterAsyncTask<BaseResponseVO>() {
            @Override
            protected BaseResponseVO doIt() {
                return BeaconPagSeguroApp.getApi().beacons(new BeaconRequestVO("00:00:00:00:00"));
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
