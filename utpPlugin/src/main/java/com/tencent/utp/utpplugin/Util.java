package com.tencent.utp.utpplugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        return str;
    }
}
