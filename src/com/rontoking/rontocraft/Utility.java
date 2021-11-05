package com.rontoking.rontocraft;

import com.badlogic.gdx.math.MathUtils;

public class Utility {

    public static int roundFloor(float num, float r){
        return (int)(r*(MathUtils.floor(num/r)));
    }

    public static String displayFloat(float f, int num) {
        String s = Float.toString(f);
        if (s.contains(".")) {
            int period = s.indexOf(".");
            if (s.length() >= period + num + 1) {
                s = s.substring(0, period + num + 1);
            }
        }
        return s;
    }

    public static void threadSleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void print(String s){
        System.out.println(s);
    }
}
