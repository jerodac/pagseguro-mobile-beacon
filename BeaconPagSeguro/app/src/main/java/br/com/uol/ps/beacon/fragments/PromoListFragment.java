package br.com.uol.ps.beacon.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.uol.ps.beacon.BeaconPagSeguroApp;
import br.com.uol.ps.beacon.R;
import br.com.uol.ps.beacon.business.BetterAsyncTask;
import br.com.uol.ps.beacon.components.BeaconBluetooth;
import br.com.uol.ps.beacon.components.RecyclerViewAdapter;
import br.com.uol.ps.beacon.others.DeviceFound;
import br.com.uol.ps.beacon.others.OffersFacade;
import br.com.uol.ps.beacon.others.OffersModel;
import br.com.uol.ps.beacon.services.NotificationService;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;
import br.com.uol.ps.beacon.vo.BaseResponseVO;
import br.com.uol.ps.beacon.vo.BeaconRequestVO;
import br.com.uol.ps.beacon.vo.BeaconResponseVO;
import retrofit.RetrofitError;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class PromoListFragment extends BaseFragment {

    public static final String TAG = "PromoListFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;

    private BeaconBluetooth beaconBluetooth;
    private BeaconBluetooth.BluetoothListener bluetoothListener;
    private Snackbar snackSearchDevices;

    /**
     * Persistence Data factory facade
     */
    private OffersFacade offersFacade;

    private void initComponents(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        offersFacade = new OffersFacade(getContext());
        mAdapter = new RecyclerViewAdapter(offersFacade.getOffers(), getContext());
        mRecyclerView.setAdapter(mAdapter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(fabOnClick());
        bluetoothListener = bluetoothListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.promo_list_fragment, container, false);
        initComponents(rootView);
        setRetainInstance(true);
        return rootView;
    }

    public View.OnClickListener fabOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBarSearchPromo();
                startFindDevices();
            }
        };
    }

    private void snackBarSearchPromo() {
        snackSearchDevices = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackSearchDevices.getView();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View snackView = inflater.inflate(R.layout.snack_bar_layout, null);

        layout.addView(snackView, 0);
        snackSearchDevices.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackSearchDevices.show();
    }

    public BeaconBluetooth.BluetoothListener bluetoothListener() {
        return new BeaconBluetooth.BluetoothListener() {
            @Override
            public void action(String action, final List<DeviceFound> devicesFounds) {
                if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_STARTED) == 0) {
                    ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_STARTED");
                } else if (action.compareTo(BeaconBluetooth.BluetoothListener.ACTION_DISCOVERY_FINISHED) == 0) {
                    ApplicationUtilities.log(Log.INFO, "Bluetooth DISCOVERY_FINISHED");
                    ApplicationUtilities.log(Log.INFO, "Bluetooth MACS: " + devicesFounds.toString());
                    if (devicesFounds.size() > 0) {
                        queryPromoBeacons(devicesFounds.get(0));
                    } else {
                        snackSearchDevices.dismiss();
                        Toast.makeText(getContext(), "Não foi encontrado novas ofertas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    /**
     * Snack  configuration, search devices (promos)
     */
    private void startFindDevices() {
        try {
            if (beaconBluetooth == null) {
                beaconBluetooth = new BeaconBluetooth(getActivity(), bluetoothListener);
            }
            beaconBluetooth.searchBeacon();
        } catch (Exception e) {
            ApplicationUtilities.log(Log.ERROR, "Beacon PagSeguro", e);
        }
    }

    /**
     * Snack configuration search finish
     */
    private void finishSearch() {
        View view = snackSearchDevices.getView();

        TextView snackText = (TextView) view.findViewById(R.id.snackbar_text);
        snackText.setText("\nOfertas encontradas!");
        snackText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        snackSearchDevices.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        snackSearchDevices.setDuration(Snackbar.LENGTH_SHORT);

    }

    private void queryPromoBeacons(final DeviceFound deviceFound) {
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

                addOfferRecyclerView(beaconResponseVO.getOffers());
                offersFacade.add(beaconResponseVO.getOffers());
                offersFacade.save(getContext());

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        NotificationService.callNotification(getActivity(),
                                "Bateu fome? O carrinho chegou... \n" +
                                        "Oferta do dia: " + beaconResponseVO.getOffers().get(0).getTitle());
                        if (snackSearchDevices != null && snackSearchDevices.isShown()) {
                            finishSearch();
                        }
                    }
                });
                mRecyclerView.scrollToPosition(0);
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
                Toast.makeText(getContext(), "Falha ao buscar ofertas", Toast.LENGTH_SHORT).show();
                snackSearchDevices.dismiss();
            }
        }.execute();
    }

    private void addOfferRecyclerView(List<OffersModel> offers) {
        for (int i = 0; i < offers.size(); i++) {
            if (offersFacade.containsOffer(offers.get(i))) {
                continue;
            }
            ((RecyclerViewAdapter) mAdapter).addItem(offers.get(i), i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, OffersModel offersModel, View v) {
                ApplicationUtilities.log(Log.INFO, " Clicked on Item " + position);
                DetailItemFragment detailItemFragment = new DetailItemFragment();
                Bundle args = new Bundle();
                args.putParcelable(DetailItemFragment.BUNDLE_ARGS, offersModel);
                detailItemFragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.replace(R.id.fragment_container, detailItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconBluetooth = null;
    }
}
