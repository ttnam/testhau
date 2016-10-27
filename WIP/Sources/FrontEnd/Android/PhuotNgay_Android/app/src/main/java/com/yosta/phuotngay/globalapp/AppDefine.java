package com.yosta.phuotngay.globalapp;

import java.util.HashMap;

/**
 * Created by HenryPhuc on 4/12/2016.
 */
public class AppDefine {

    public static final int MIN_DISTANCE = 0;
    public static final String BUNDLE_EVENT = "EVENT";
    public static final String BUNDLE_ORGANIZATION = "ORGANIZATION";
    private static final HashMap<Integer, String> _MONTH_IN_YEAR = new HashMap<>();

    private AppDefine() {

    }

    public enum FilterType {
        All,
        Happening,
        ComingUp
    }

    public static HashMap<Integer, String> MONTH_IN_YEAR() {

        _MONTH_IN_YEAR.put(1, "JAN");
        _MONTH_IN_YEAR.put(2, "FEB");
        _MONTH_IN_YEAR.put(3, "MAR");
        _MONTH_IN_YEAR.put(4, "APR");
        _MONTH_IN_YEAR.put(5, "MAY");
        _MONTH_IN_YEAR.put(6, "JUNE");
        _MONTH_IN_YEAR.put(7, "JULY");
        _MONTH_IN_YEAR.put(8, "AUG");
        _MONTH_IN_YEAR.put(9, "SEP");
        _MONTH_IN_YEAR.put(10, "OCT");
        _MONTH_IN_YEAR.put(11, "NOV");
        _MONTH_IN_YEAR.put(12, "DEC");

        return _MONTH_IN_YEAR;
    }
}
