package com.dss886.utils.tools;

import com.dss886.utils.log.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by dss886 on 14/10/29.
 */
public class CountLine {

    public static void count(String srcPath, String type) {
        type = "." + type;
        File f = new File(srcPath);
        int lineNum = findJava(f.getPath(), type);
        LogUtils.v("代码统计", srcPath + " 目录下所有的java文件共有 " + lineNum + " 行代码");
    }

    public static int findJava(String path, String type){
        int lineNum = 0;
        File f = new File(path);
        if(f.isDirectory()){
            File[] fileList=f.listFiles();
            if(fileList==null)
                return 0;
            for(File a:fileList){
                lineNum += findJava(a.getPath(), type);
            }
        }else{
            if(f.getPath().endsWith(type)){
                try {
                    Scanner sc=new Scanner(f);
                    while(sc.hasNext()){
                        sc.nextLine();
                        lineNum++;
                    }
                    sc.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return lineNum;
    }
}
