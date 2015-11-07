package br.com.uol.ps.beacon.components;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.uol.ps.beacon.BeaconPagSeguroApp;
import br.com.uol.ps.beacon.MainActivity;
import br.com.uol.ps.beacon.R;
import br.com.uol.ps.beacon.fragments.PromoListFragment;
import br.com.uol.ps.beacon.fragments.SetupFragment;
import br.com.uol.ps.beacon.vo.BaseResponseVO;
import br.com.uol.ps.beacon.business.BetterAsyncTask;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;
import br.com.uol.ps.beacon.vo.BeaconRequestVO;
import br.com.uol.ps.beacon.vo.BeaconResponseVO;
import retrofit.RetrofitError;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class NavComponent {

    /**
     * DrawerLayout
     */
    private DrawerLayout mDrawerLayout;

    /**
     * DrawerToggle
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Activity da aplicação
     */
    private Activity mActivity;

    /**
     * Fragment Manager
     */
    private FragmentManager fragmentManager;

    private Toolbar toolbar;


    public NavComponent(Activity activity) {
        mActivity = activity;
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                activity,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerLayout.findViewById(R.id.drawer_button_logout).setOnClickListener(onLogoutClick());
        fragmentManager = ((MainActivity) mActivity).getSupportFragmentManager();

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemListener);

    }


    private View.OnClickListener onLogoutClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                mActivity.finish();
                                break;
                        }
                    }
                };
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                ApplicationUtilities.showQuestionDialog(mActivity,
                        mActivity.getResources().getString(R.string.dialog_change_user), dialogClickListener);
            }
        };
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            ((AppCompatActivity) mActivity).getSupportActionBar().setHomeButtonEnabled(true);

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();

            ((AppCompatActivity) mActivity).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) mActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    NavigationView.OnNavigationItemSelectedListener navigationItemListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_promo:
                            clearBackStack();
                            Fragment promoListFragment = fragmentManager.findFragmentByTag(PromoListFragment.TAG);
                            if (promoListFragment != null && !promoListFragment.isVisible()) {
                                promoListFragment = new PromoListFragment();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, promoListFragment).addToBackStack(null).commit();
                                Toast.makeText(mActivity.getApplicationContext(), "Ofertas", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.nav_fav:
                            Toast.makeText(mActivity.getApplicationContext(), "Fluxo não implementado", Toast.LENGTH_SHORT).show();
                            serviceTest();
                            break;
                        case R.id.nav_previous_offer:
                            Toast.makeText(mActivity.getApplicationContext(), "Fluxo não implementado", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_call:
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "pagseguro.testes@gmail.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PagSeguro BEACON Poc");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Escreva o seu comentário para a equipe de aplicativos PagSeguro.");
                            mActivity.startActivity(Intent.createChooser(emailIntent, "Enviando e-mail"));
                            break;
                        case R.id.nav_config:
                            SetupFragment setupFragment = new SetupFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, setupFragment, "Configurações").addToBackStack(null).commit();
                            Toast.makeText(mActivity.getApplicationContext(), "Fluxo não implementado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            };

    //clear back stack
    private void clearBackStack() {
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    private BetterAsyncTask<?> mCurrentTask;

    private void serviceTest() {
        runAsync(new BetterAsyncTask<BaseResponseVO>() {
            @Override
            protected BaseResponseVO doIt() {
                return BeaconPagSeguroApp.getApi().beacons(new BeaconRequestVO("00:07:80:79:18:F0"));
            }

            @Override
            protected void onResult(BaseResponseVO result) {
                ApplicationUtilities.log(Log.INFO, "OnResult");
                BeaconResponseVO beaconResponseVO = (BeaconResponseVO) result;
                ApplicationUtilities.log(Log.INFO, "Result: " + beaconResponseVO.toString());
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
                    e.printStackTrace();
                }
                if (response != null) {
                    //mState.getData().setEmail(mEmail.getValue());
                    //mState.setStep(FlowStep.EMAIL_ALREADY_REGISTERED);
                } else {
                    //showOnDialog(R.string.error_network);
                }
                //validateForm();
                //mEmail.requestFocus();
            }
        });
    }


    protected void runAsync(BetterAsyncTask<?> asyncTask) {
        //mDialog = new LoadingDialog(getContext());
        //mDialog.show();
        mCurrentTask = asyncTask;
        mCurrentTask.addExitListener(new Runnable() {
            @Override
            public void run() {
//                if (getContext() != null) {
//                    if (mDialog != null) {
//                        mDialog.dismiss();
//                        mDialog = null;
//                    }
//                }
            }
        });
        mCurrentTask.execute();
    }


}
