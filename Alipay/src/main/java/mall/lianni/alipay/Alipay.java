package mall.lianni.alipay;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;

import java.util.Map;

import mall.lianni.alipay.PayResult.Result;


public class Alipay {
    
    private static Alipay alipay;
    
    public static final Alipay Init(Context context) {
        if (alipay == null) {
            alipay = new Alipay(context);
        }
        return alipay;
    }
    
    public static Alipay getInstance() {
        return alipay;
    }
    
    private Alipay(Context context) {
    
    }
    
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Alipay.SDK_PAY_FLAG: {
                    Map<String, String> mapResult = (Map<String, String>) msg.obj;
                    
                    PayResult payResult = new PayResult();
                    payResult.setMemo(mapResult.get("memo"));
                    payResult.setResultStatus(mapResult.get("resultStatus"));
                    payResult.setResult(new Gson().fromJson(mapResult.get("result"), Result.class));
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
//                    String resultInfo = payResult.getResult().getAlipayTradeAppPayResponse();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (getInstance().callBack != null) {
                            getInstance().callBack.onPaySuccess(payResult);
                        }
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        //操作取消
                        if (getInstance().callBack != null) {
                            getInstance().callBack.onPayCancel(payResult);
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            if (getInstance().callBack != null) {
                                getInstance().callBack.onPayConfirming(payResult);
                            }
                        } else {
                            if (getInstance().callBack != null) {
                                getInstance().callBack.onPayFailed(payResult);
                            }
                        }
                    }
                    
                    break;
                }
            }
        }
    };
    
    OnPayCallBack callBack;
    
    
    public interface OnPayCallBack {
        /**
         * 支付成功
         *
         * @param payResult
         */
        void onPaySuccess(PayResult payResult);
        /**
         * 支付失败
         *
         * @param payResult
         */
        void onPayFailed(PayResult payResult);
        /**
         * 支付确认中
         *
         * @param payResult
         */
        void onPayConfirming(PayResult payResult);
        void onPayCancel(PayResult payResult);
    }
    
    
    public final static int SDK_PAY_FLAG = 1;
    
    /**
     * 支付
     *
     * @param activity
     * @param onPayCallBack
     */
    public void pay(final Activity activity, final String payInfo, @Nullable OnPayCallBack onPayCallBack) {
        this.callBack = onPayCallBack;
        
        Runnable payRunnable = new Runnable() {
            
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo, true);
                
                Map<String, String> result = alipay.payV2(payInfo, true);
                
                Message msg = new Message();
                msg.what = Alipay.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void pay(final Activity activity,final PayModel payModel,@Nullable OnPayCallBack onPayCallBack){

    }
}
