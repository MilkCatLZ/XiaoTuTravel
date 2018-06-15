package shy.car.sdk.app;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.base.databinding.BindingAdapter;
import com.base.network.glide.GlideRoundTransform;
import com.base.util.ImageUtil;
import com.base.util.Log;
import com.base.util.anim.AnimationManager;
import com.bumptech.glide.Glide;

import shy.car.sdk.BuildConfig;
import shy.car.sdk.R;


/**
 * Created by Syokora on 2016/8/27.
 */
public class DataBindingAdapter extends BindingAdapter {

    @android.databinding.BindingAdapter("android:imageURL")
    public static void setUrlImage(@NonNull ImageView imageView, String url) {
        try {
            if (!url.contains(BuildConfig.Host)) {
                url = BuildConfig.Host + url;
            }
            if (ImageUtil.hasImage(url)) {
                Glide.with(imageView.getContext())
                        .load(url)
                        .error(-1)
                        .crossFade()
                        .centerCrop()
                        .into(imageView);
            } else {
                Glide.with(imageView.getContext())
                        .load(-1)
                        .into(imageView);
            }
        } catch (Exception ignored) {

        }
    }


    @android.databinding.BindingAdapter("android:appAvatarURL")
    public static void setRoundImage(@NonNull ImageView imageView, @Nullable String url) {
        try {
            if (!url.contains(BuildConfig.Host)) {
                url = BuildConfig.Host + url;
            }
            if (ImageUtil.hasImage(url)) {
                Glide.with(imageView.getContext())
                        .load(url)
                        .crossFade()
                        .error(R.drawable.icon_pre_header)
                        .transform(new GlideRoundTransform(imageView.getContext(), 120))
                        .into(imageView);
            } else {
                Glide.with(imageView.getContext())
                        .load(R.drawable.icon_pre_header)
                        .transform(new GlideRoundTransform(imageView.getContext(), 120))
                        .into(imageView);
            }
        } catch (Exception ignored) {

        }
        Log.d("LNDataBindingAdapter", "setRoundImage");
    }

    static final int duration = 120;

    /**
     * 动画从左到右消失，消失时为圆形
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGoneLtoR_normal")
    public static void setViewVisiblesOrGoneLeftToRightNormal(@NonNull final View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                AnimationManager.animFromLeftToRightDisappearNormalReverse(view, duration, null);
            }
        } else {
            if (view.getVisibility() != View.GONE) {
                AnimationManager.animFromLeftToRightDisappearNormal(view, duration, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
            }
        }
        Log.d("LNDataBindingAdapter", "setViewVisiblesOrGoneLeftToRightNormal");
    }

    /**
     * 动画从左到右消失，消失时为圆形
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGoneLtoR_full")
    public static void setViewVisiblesOrGoneLeftToRightFull(@NonNull final View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                AnimationManager.animFromLeftToRightDisappearFullReverse(view, duration, null);
            }
        } else {
            if (view.getVisibility() != View.GONE) {
                AnimationManager.animFromLeftToRightDisappearFull(view, duration, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
            }
        }
        Log.d("LNDataBindingAdapter", "setViewVisiblesOrGoneLeftToRightFull");
    }

    /**
     * 动画右到左消失，消失时为圆形
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGoneRtoL_normal")
    public static void setViewVisiblesOrGoneRightToLeftNormal(@NonNull final View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                AnimationManager.animFromRightToLeftDisappearNormalReverse(view, duration, null);
            }
        } else {
            if (view.getVisibility() != View.GONE) {
                AnimationManager.animFromRightToLeftDisappearNormal(view, duration, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
            }
        }
        Log.d("LNDataBindingAdapter", "setViewVisiblesOrGoneRightToLeftNormal");
    }

    /**
     * 动画从右到左消失，消失时为圆形
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGoneRtoL_full")
    public static void setViewVisiblesOrGoneRightToLeftFull(@NonNull final View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
                AnimationManager.animFromRightToLeftDisappearFullReverse(view, duration, null);
            }
        } else {
            if (view.getVisibility() != View.GONE) {
                AnimationManager.animFromRightToLeftDisappearFull(view, duration, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
            }
        }
        Log.d("LNDataBindingAdapter", "setViewVisiblesOrGoneRightToLeftFull");
    }

    /**
     * 动画从右到左消失，消失时为圆形
     *
     * @param view
     * @param alpha
     */
    @android.databinding.BindingAdapter("android:backgroundAlpha")
    public static void setBackgroundAlpha(@NonNull final View view, @NonNull int alpha) {
        if (alpha >= 0 && alpha <= 255) {
            view.getBackground()
                    .setAlpha(alpha);
        }
        Log.d("LNDataBindingAdapter", "setBackgroundAlpha");
    }


}
