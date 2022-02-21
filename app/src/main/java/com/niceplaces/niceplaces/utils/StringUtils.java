package com.niceplaces.niceplaces.utils;


import android.util.Log;

import java.util.List;

public class StringUtils {

    public static String listToString(List<Integer> list){
        String string = "";
        for (int i = 0; i < list.size(); i++){
            string = string.concat(Integer.toString(list.get(i)));
        }
        return string;
    }

    public static void printList(List<String> list){
        for (int i = 0; i < list.size(); i++){
            Log.i("STRING_ARRAY", "[" + i + "] " + list.get(i));
        }
    }
}
