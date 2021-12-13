package com.jumarni.appassetdesa.helper;


import java.text.NumberFormat;
import java.util.Locale;

public class FormatRupiah {
    public String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
