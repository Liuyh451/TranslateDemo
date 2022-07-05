package com.example.finalapp;

import android.view.View;

public class Convert {
    private static String result;
    /**
 * 语言类型选择
 */
    public static String conv(String str) {
        switch (str){
            case "自动检测":
                result="auto";
                break;
            case "中文":
                result="zh";
                break;
            case "英语":
                result="en";
                break;
            case "繁体中文":
                result="cht";
                break;
            case "日语":
                result="jp";
                break;
            default :
                result="auto";
        }
        return result;

}

}
