package com.dss886.utils;

import com.dss886.utils.http.GetMethod;
import com.dss886.utils.log.LogUtils;

import java.io.IOException;

/**
 * Created by dss886 on 14/10/22.
 */
public class Main {
    public static void main(String... args) {
//        CountLine.count("/Users/dss886/workspace", "java");
        GetMethod getMethod = new GetMethod();
        try {
            LogUtils.d("baidu", getMethod.execute("http://www.baidu.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
