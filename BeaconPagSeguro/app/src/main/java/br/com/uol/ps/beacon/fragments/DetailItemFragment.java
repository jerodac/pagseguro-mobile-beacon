package br.com.uol.ps.beacon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

import br.com.uol.ps.beacon.R;
import br.com.uol.ps.beacon.components.ItemCalculator;
import br.com.uol.ps.beacon.others.OffersModel;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;
import br.com.uol.ps.beacon.utils.Utils;

/**
 * Fragment dos detalhes dos itens
 *
 * @author Jean Rodrigo D. Cunha
 */
public class DetailItemFragment extends BaseFragment {

    public static final String BUNDLE_ARGS = "ARGS";

    private TextView tvTotal;
    private TextView tvDescribe;
    private TextView tvDate;
    private ImageView img;
    private TextView tvQtdItem;
    private ItemCalculator itemCalculator;
    private TextView tvUnityValue;
    private ImageButton btnMinus;
    private ImageButton btnPlus;
    private Button btnFavorite;

    @Override
    public void initComponents() {
        tvTotal = (TextView) rootView.findViewById(R.id.tv_total);
        tvDescribe = (TextView) rootView.findViewById(R.id.tv_describe);
        img = (ImageView) rootView.findViewById(R.id.img);
        tvQtdItem = (TextView) rootView.findViewById(R.id.tv_qtd_item);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        tvUnityValue = (TextView) rootView.findViewById(R.id.tv_unity_value);
        btnMinus = (ImageButton) rootView.findViewById(R.id.btn_minus_item);
        btnMinus.setOnClickListener(onMinusItem());
        btnPlus = (ImageButton) rootView.findViewById(R.id.btn_plus_item);
        btnPlus.setOnClickListener(onPlusItem());
        btnFavorite = (Button) rootView.findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(onClickFavorite());
        super.initComponents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detail_item_fragment, container, false);
        initComponents();
        getNavComponent().setDrawerState(false);
        populateView();
        return rootView;
    }

    private void populateView() {
        OffersModel offersModel = getArguments().getParcelable(DetailItemFragment.BUNDLE_ARGS);
        ApplicationUtilities.log(Log.INFO, "Args obj: " + offersModel.toString());
        itemCalculator = new ItemCalculator(offersModel.getValue());
        itemCalculator.setOnEventListener(onChangeValue());
        getNavComponent().setTitle(offersModel.getTitle());
        tvTotal.setText(Utils.bigDecimalToMonetary(offersModel.getValue()));
        tvUnityValue.setText(Utils.bigDecimalToMonetary(offersModel.getValue()));
        tvDescribe.setText(offersModel.getTitle().toUpperCase());
        Picasso.with(getContext())
                .load(offersModel.getImageResource())
                .into(img);
        tvDate.setText(offersModel.getTime());
    }

    private ItemCalculator.OnEventListener onChangeValue() {
        return new ItemCalculator.OnEventListener() {
            @Override
            public void onValueChange(BigDecimal value, int quantity) {
                ApplicationUtilities.log(Log.INFO, "onChangeValue: " + value);
                tvTotal.setText(Utils.bigDecimalToMonetary(value));
                tvQtdItem.setText(String.valueOf(quantity));
            }
        };
    }

    private View.OnClickListener onPlusItem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCalculator.addItem();
            }
        };
    }

    private View.OnClickListener onMinusItem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCalculator.removeItem();
            }
        };
    }

    private View.OnClickListener onClickFavorite() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Fluxo não implementado", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getNavComponent().setDrawerState(true);
        getNavComponent().setTitle(getResources().getString(R.string.app_name));
    }
}
