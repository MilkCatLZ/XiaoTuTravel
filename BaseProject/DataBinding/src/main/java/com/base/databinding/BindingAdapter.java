package com.base.databinding;


import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableBoolean;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.base.widget.UltimateRecyclerView;
import com.base.widget.UltimateViewAdapter;


/**
 * Created by Syokora on 2016/8/19.
 * Adapter的绑定
 * View的visible、invisible、gone
 */
public class BindingAdapter {
    
    @androidx.databinding.BindingAdapter("android:adapter")
    public static <T extends ListAdapter & Filterable> void setAdapter(@NonNull AutoCompleteTextView autoCompleteTextView, @Nullable T adapter) {
        autoCompleteTextView.setAdapter(adapter);
    }
    
    @androidx.databinding.BindingAdapter("android:onItemClickListener")
    public static void setOnItemClickListener(@NonNull AutoCompleteTextView autoCompleteTextView, @Nullable OnItemClickListener listener) {
        autoCompleteTextView.setOnItemClickListener(listener);
    }
    
    @androidx.databinding.BindingAdapter("android:bindAdapter")
    public static <T extends UltimateViewAdapter> void setUltimateRecyclerViewAdapter(@NonNull UltimateRecyclerView recyclerView, @Nullable T adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }
    
    @androidx.databinding.BindingAdapter("android:bindAdapter")
    public static <T extends UltimateViewAdapter> void setRecyclerViewAdapter(@NonNull RecyclerView recyclerView, @Nullable T adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }
    
    
    /**
     * 这个只处理visible和invisible
     *
     * @param view
     * @param visible
     */
    @androidx.databinding.BindingAdapter("android:visibleOrInvisible")
    public static void setVisibleses(@NonNull View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.INVISIBLE)
                view.setVisibility(View.INVISIBLE);
        }
    }
    
    /**
     * 这个只处理visible和gone
     *
     * @param view
     * @param visible
     */
    @androidx.databinding.BindingAdapter("android:visibleOrGone")
    public static void setViewVisiblesOrGones(@NonNull View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.GONE)
                view.setVisibility(View.GONE);
        }
    }
    
    /**
     * 这个只处理visible和gone
     *
     * @param view
     * @param visible
     */
    @androidx.databinding.BindingAdapter("android:visibleOrGone")
    public static void setViewGroupVisiblesOrGone(@NonNull ViewGroup view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.GONE)
                view.setVisibility(View.GONE);
        }
    }
    
    /**
     * 添加下划线
     *
     * @param textView
     * @param d
     */
    @androidx.databinding.BindingAdapter("android:deleteLines")
    public static void setTextViewDeleteLine(@NonNull TextView textView, @NonNull boolean d) {
        if (d)
            textView.getPaint()
                    .setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    
    /**
     * 添加下划线
     *
     * @param textView
     * @param d
     */
    @androidx.databinding.BindingAdapter("android:underLine")
    public static void setTextViewUnderLine(@NonNull TextView textView, @NonNull boolean d) {
        if (d)
            textView.getPaint()
                    .setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    
    /**
     * 添加下划线
     *
     * @param radioButton
     * @param style
     */
    @androidx.databinding.BindingAdapter("android:typeFaces")
    public static void setTextViewTypeFaces(@NonNull TextView radioButton, @StyleRes int style) {
        radioButton.setTypeface(Typeface.defaultFromStyle(style));
    }
    
    @BindingConversion
    public static boolean convertBindableToBoolean(ObservableBoolean observableBoolean) {
        return observableBoolean.get();
    }
}