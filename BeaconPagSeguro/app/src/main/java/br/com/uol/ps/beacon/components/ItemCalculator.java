package br.com.uol.ps.beacon.components;

import java.math.BigDecimal;

/**
 * Item Calculator
 *
 * @Jean Rodrigo Dalbon Cunha
 */
public class ItemCalculator {

    private BigDecimal value;
    private int quantity = 1;
    private OnEventListener mOnEventListener;

    public ItemCalculator(BigDecimal value) {
        this.value = value;
    }

    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }


    public void addItem() {
        quantity++;
        BigDecimal valueCalc = value.multiply(new BigDecimal(quantity));
        mOnEventListener.onValueChange(valueCalc, quantity);
    }

    public void removeItem() {
        if (quantity > 1) {
            quantity--;
            BigDecimal valueCalc = value.multiply(new BigDecimal(quantity));
            mOnEventListener.onValueChange(valueCalc, quantity);
        }
    }

    public interface OnEventListener {
        void onValueChange(BigDecimal value, int quantity);
    }

}
