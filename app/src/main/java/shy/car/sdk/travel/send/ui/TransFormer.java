package shy.car.sdk.travel.send.ui;


import androidx.viewpager.widget.ViewPager;
import android.view.View;

import shy.car.sdk.R;

public class TransFormer implements ViewPager.PageTransformer {
    private static float defaultScale =  7.0f /  15.0f;

    @Override
    public void transformPage(View view, float position) {
        View img = view.findViewById(R.id.img_car);

        int diffWidth = (view.getWidth() - img.getWidth()) / 2;

        if (position < -1) { // [-Infinity,-1)
            img.setScaleX(defaultScale);
            img.setScaleY(defaultScale);
            img.setTranslationX(diffWidth);

        } else if (position <= 0) { // [-1,0]
            img.setScaleX((float) 1 + position / (float) 15);
            img.setScaleY((float) 1 + position / (float) 15);
            img.setTranslationX((0 - position) * diffWidth);

        } else if (position <= 1) { // (0,1]
            img.setScaleX((float) 1 - position / (float) 15);
            img.setScaleY((float) 1 - position / (float) 15);
            img.setTranslationX((0 - position) * diffWidth);

        } else { // (1,+Infinity]
            img.setScaleX(defaultScale);
            img.setScaleY(defaultScale);
            img.setTranslationX(-diffWidth);
        }
    }
}