package br.com.uol.ps.beacon.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by jeanrodrigo on 30/10/15.
 */
public class Utils {

    public static String bigDecimalToMonetary(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(value).replace("R$", "R$ ");
    }
}
