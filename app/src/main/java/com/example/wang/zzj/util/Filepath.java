package com.example.wang.zzj.util;

import java.io.File;
import java.util.Vector;
  
public class Filepath {  
  

    public static Vector<String> GetTestXlsFileName(String fileAbsolutePath) {  
        Vector<String> vecFile = new Vector<String>();  
        File file = new File(fileAbsolutePath);  
        File[] subFile = file.listFiles();  
  
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
            // 判断是否为文件夹  
            if (!subFile[iFileLength].isDirectory()) {  
                String fileName = subFile[iFileLength].getName();  
                // 判断是否为.txt结尾  
                if (fileName.trim().toLowerCase().endsWith(".jpg")|fileName.trim().toLowerCase().endsWith(".png")) {
                    vecFile.add(fileName);  
                }  
            }  
        }  
        return vecFile;  
    }  
}  