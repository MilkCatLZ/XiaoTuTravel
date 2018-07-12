package shy.car.sdk.app.util;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.TextView;

import shy.car.sdk.travel.order.data.RentOrderDetail;
import shy.car.sdk.travel.rent.data.RentOrderState;


/**
 * Created by LZ on 2017/9/4.
 * 订单倒计时操作
 */
public class CountUpThread {


    private final RentOrderDetail orderDetailModel;
    private long time;
    private boolean cancel;
//    private FinishListener finishListener;
//
//    public void setFinishListener(FinishListener finishListener) {
//        this.finishListener = finishListener;
//    }
//
//    public interface FinishListener {
//        void countDownFinish();
//    }

    /**
     * @param orderDetailModel
     * @param time             剩余时间 单位：秒
     */
    public CountUpThread(RentOrderDetail orderDetailModel, long time) {
        this.orderDetailModel = orderDetailModel;
        this.time = time;
    }


    public void start(final TextView textView) {
        CountUpThread thread = null;
        try {
            thread = (CountUpThread) textView.getTag();
        } catch (Exception e) {

        }
        //停止旧的线程
        if (thread != null) {
            thread.cancel();
        }

        if (orderDetailModel.getStatus() != RentOrderState.Create || time <= 0) {
            return;
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void run() {
                if (cancel) {
                    return;
                }
                long tt = ++time;

                long h = tt / 60 / 60;//小时
                long t1 = tt % 3600;
                long mi = t1 / 60;//分
                long s = t1 % 60;//秒

                if (time <= 0) {
                    cancel();
//                    if (finishListener != null)
//                        finishListener.countDownFinish();
                    return;
                }


                String hour = h > 0 ? h + "时" : "";
                String minute = mi > 0 ? mi + "分" : "";
                String second = s + "秒";
                if (textView != null) {
                    textView.setText(hour + minute + second);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(runnable, 0);

        try {
            textView.setTag(this);
        } catch (Exception e) {

        }
    }

    public void cancel() {
        cancel = true;
    }
}
