package com.vondear.rxtools.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jk on 2018/5/16.
 */
public class RxOpenRawFile {


    public static String getFromRaw(InputStream in) {
        try {
            InputStreamReader inputReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
