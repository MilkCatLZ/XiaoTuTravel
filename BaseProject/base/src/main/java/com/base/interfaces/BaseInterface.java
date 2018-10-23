package com.base.interfaces;


import android.app.Application;
import android.content.res.Resources;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lz on 16-1-21.
 * 这个接口用于统一Activity和Fragment的行为
 */
public interface BaseInterface {

    CharSequence getTitleText();

    ViewDataBinding getViewDataBinds();

    @Keep
    EventBus getEventBusDefault();

    /**
     * 将Object注册到全局EventBus
     *
     * @param o
     */
    @Keep
    void register(@NonNull Object o);

    void postSticky(@NonNull Object o);

    boolean isActive();

    ActionBar getSupportActionBar();

    Resources getResources();

    void setSupportActionBar(@NonNull Toolbar toolbar);
    
    <T extends View> T findViewById(@IdRes int id);

    void finish();

    Application getApplication();

    void setStatusBarColor(@ColorRes int color);

    void hideSoftKeyboard();

    void setHomeIndicator(@DrawableRes int homeIndicator);
}
