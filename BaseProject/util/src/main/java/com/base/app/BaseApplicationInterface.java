package com.base.app;


import android.support.v7.app.AppCompatActivity;


/**
 * Created by LZ on 2017/3/20.
 */

public interface BaseApplicationInterface {
    void addActivity(AppCompatActivity activity);
    void removeActivity(AppCompatActivity activity);
    void exit();
}
