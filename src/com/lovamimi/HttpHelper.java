package com.lovamimi;

public class HttpHelper {

    public static String chop(String s) {
        if (s.isEmpty()) {
            return s;
        } else {
            if (s.charAt(s.length() - 1) == '\n') {
                return s.substring(0, s.length() - 1);
            } else {
                return s;
            }
        }
    }
}
