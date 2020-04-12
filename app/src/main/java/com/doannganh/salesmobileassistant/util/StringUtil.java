package com.doannganh.salesmobileassistant.util;

import android.content.Context;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class StringUtil {

    /**
     * Get price with VietNam currency
     *
     * @param context
     * @param price
     * */
    public static String formatVnCurrence(Context context, String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        //price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s đ", price);
        return price;
    }

    public static double parseDoubleFromCurrence(String price){
        // can 4,000.00 d
        String copy = price;
        copy = copy.replaceAll(",|đ", "");
        try {
            return Double.parseDouble(copy);
        } catch (Exception e){
            return -1;
        }

    }

    public static boolean isNullOrEmpty(String string){
        if(string == null || string.equals(ConstantUtil.INIT_STRING_EMPTY) || string.toLowerCase().equals("null")
                || string.isEmpty())
            return true;
        return false;
    }
}
