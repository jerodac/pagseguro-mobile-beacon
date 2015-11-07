package br.com.uol.ps.beacon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.uol.ps.beacon.MainActivity;
import br.com.uol.ps.beacon.components.NavComponent;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class BaseFragment extends Fragment {

    /**
     * Root View
     */
    public View rootView;

    public void initComponents() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initComponents();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public NavComponent getNavComponent() {
        return ((MainActivity) getActivity()).getNavComponent();
    }
}
