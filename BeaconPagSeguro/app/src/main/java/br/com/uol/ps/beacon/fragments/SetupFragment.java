package br.com.uol.ps.beacon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.uol.ps.beacon.R;

/**
 * Created by jeanrodrigo on 30/10/15.
 */
public class SetupFragment extends BaseFragment {

    @Override
    public void initComponents() {
        super.initComponents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.config_fragment, container, false);
        initComponents();
        return rootView;
    }
}
