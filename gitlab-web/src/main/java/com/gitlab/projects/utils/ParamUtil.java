package com.gitlab.projects.utils;

import java.text.NumberFormat;
import java.text.ParseException;

public class ParamUtil {

    static public Long longValue(String strValue) {
        Long reValue = null;

        if ((strValue == null) || (strValue.trim().equals(""))) {
            strValue = null;
        }

        if (strValue == null) {
            return null;
        }

        NumberFormat nf = NumberFormat.getInstance();

        try {
            reValue = (Long) nf.parse(strValue).longValue();
        } catch (ParseException ex) {
        }

        return reValue;
    }

    static public Integer integerValue(Object strValue) {
        return integerValue((strValue != null)
                ? strValue.toString()
                : null);
    }

    static public Long longValue(Object strValue) {
        return longValue((strValue != null)
                ? strValue.toString()
                : null);
    }

    static public Integer integerValue(String strValue) {
        Integer reValue = null;

        if ((strValue == null) || (strValue.trim().equals(""))) {
            strValue = null;
        }

        if (strValue == null) {
            return null;
        }

        NumberFormat nf = NumberFormat.getInstance();

        try {
            reValue = (Integer) nf.parse(strValue).intValue();
        } catch (ParseException ex) {
        }

        return reValue;
    }

    private ParamUtil() {
    }
    
}
