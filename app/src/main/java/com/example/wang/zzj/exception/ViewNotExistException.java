package com.example.wang.zzj.exception;
/**
 * *****************************************************
 * Copyright (C) Dayan technology Co.ltd - All Rights Reserved
 * <p/>
 * This file is part of Dayan technology Co.ltd property.
 * <p/>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * *****************************************************
 */

/**
 * Created by kaiyi on 11/7/15.View不存在异常
 */
public class ViewNotExistException extends Exception {

    String className;

    public ViewNotExistException(String className) {
        super("int class : " + className + " Vew not exist");
    }
}
