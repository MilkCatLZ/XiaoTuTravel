package com.base.app;


import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by LZ on 2017/3/20.
 */

public interface BaseApplicationInterface {
    void addActivity(AppCompatActivity activity);
    void removeActivity(AppCompatActivity activity);
    void exit();
}
