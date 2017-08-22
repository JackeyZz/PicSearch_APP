package com.example.wang.zzj.util;

import java.io.Serializable;

/**
 * Created by Betula on 2016/3/22.
 */
public class ImageItem implements Serializable {

    private String path;
    private double hsvsort;
    private double siftsort;
    private double result;


    public double getHsvsort() {
        return hsvsort;
    }

    public void setHsvsort(double hsvsort) {
        this.hsvsort = hsvsort;
    }

    public double getSiftsort() {
        return siftsort;
    }

    public void setSiftsort(double siftsort) {
        this.siftsort = siftsort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
