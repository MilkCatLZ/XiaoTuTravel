package com.wq.photo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.base.util.Log;


public class GalleryBaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_clear_black_18dp);
            DrawableCompat.setTint(drawable, getResources().getColor(android.R.color.white));
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        } catch (Exception e) {
            Log.e("no Action bar","");
        }
    }

}
