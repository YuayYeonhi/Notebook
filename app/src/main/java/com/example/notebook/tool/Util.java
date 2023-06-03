package com.example.notebook.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {


    //获取格式化时间
    public static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(time));
    }

}
