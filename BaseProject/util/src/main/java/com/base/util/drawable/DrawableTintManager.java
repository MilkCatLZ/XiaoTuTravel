package com.base.util.drawable;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;


/**
 * Created by LZ on 2016/9/28.
 */

public class DrawableTintManager {
    /**
     * 着色ColorStateList
     *
     * @param drawable
     * @param colorStateList
     *
     * @return 着色后的Drawable, 兼容到4.0
     */
    public static Drawable tintColorList(@Nullable Drawable drawable, @Nullable ColorStateList colorStateList) {
        if (drawable != null && colorStateList != null) {
            drawable = DrawableCompat.wrap(drawable);
            if (VERSION.SDK_INT > VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTintList(drawable, colorStateList);
            } else {
                DrawableCompat.setTintList(drawable.mutate(), colorStateList);
            }
        }
        return drawable;
    }

    /**
     * 单一着色
     *
     * @param drawable
     * @param color
     *
     * @return 着色后的Drawable, 兼容到4.0
     */
    public static Drawable tintColor(@Nullable Drawable drawable, @ColorInt int color) {
        if(drawable!=null) {
            drawable = DrawableCompat.wrap(drawable);
            if (VERSION.SDK_INT > VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTint(drawable, color);
            } else {
                DrawableCompat.setTint(drawable.mutate(), color);
            }
        }
        return drawable;
    }
}
