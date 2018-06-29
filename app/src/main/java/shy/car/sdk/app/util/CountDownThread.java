package shy.car.sdk.app.util;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.TextView;

import java.util.Date;

import shy.car.sdk.travel.order.data.RentOrderDetail;
import shy.car.sdk.travel.rent.data.RentOrderState;


/**
 * Created by LZ on 2017/9/4.
 * 订单倒计时操作
 */
public class CountDownThread {
    private final RentOrderDetail orderDetailModel;
    private long time;
    private boolean cancel;

    /**
     * @param orderDetailModel
     * @param time             剩余时间 单位：秒
     */
    public CountDownThread(RentOrderDetail orderDetailModel, long time) {
        this.orderDetailModel = orderDetailModel;
        this.time = time;
    }


    public void start(final TextView textView) {
        CountDownThread thread = null;
        try {
            thread = (CountDownThread) textView.getTag();
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
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (cancel) {
                    return;
                }
                long tt = --time;

                long h = tt / 60;//小时
                long t1 = tt % 3600;
                long mi = t1 / 60;//分
                long s = t1 % 60;//秒

                if (time <= 0) {
                    textView.setText("0秒");
                    return;
                }


                String hour = h > 0 ? h + "时" : "";
                String minute = mi > 0 ? mi + "分" : "";
                String second = s + "秒";
                textView.setText(hour + minute + second);

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 0);

        textView.setTag(this);
    }

    public void cancel() {
        cancel = true;
    }
}
