package shy.car.sdk.travel.pay

import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonObject
import com.tencent.mm.opensdk.modelpay.PayReq
import mall.lianni.alipay.Alipay
import mall.lianni.alipay.PayResult
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.eventbus.PayCancel
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.pay.data.PayMethod

class WXPayUtil {
    companion object {
        /**
         * @return true:第三方支付  false:余额支付
         */
        fun pay(activity: XTBaseActivity, payMethod: PayMethod, results: JsonObject): Boolean {
            when {
                payMethod.name.contains("支付宝") -> {
                    Alipay.getInstance()
                            .pay(activity, results.get("pay_info").asString, object : Alipay.OnPayCallBack {
                                override fun onPaySuccess(payResult: PayResult?) {

                                    EventBus.getDefault()
                                            .post(PaySuccess())
                                }

                                override fun onPayFailed(payResult: PayResult?) {

                                }

                                override fun onPayConfirming(payResult: PayResult?) {

                                }

                                override fun onPayCancel(payResult: PayResult?) {
                                    EventBus.getDefault()
                                            .post(PayCancel())
                                }
                            })
                    return true
                }
                payMethod.name.contains("微信") -> {
                    val req = PayReq()
                    var result = results.getAsJsonObject("pay_info")

                    req.appId = result.get("appid")
                            .toString()
                    req.partnerId = result.get("partnerid")
                            .toString()
                    req.prepayId = result.get("prepayid")
                            .toString()
                    req.nonceStr = result.get("noncestr")
                            .toString()
                    req.timeStamp = result.get("timestamp")
                            .toString()
                    req.packageValue = result.get("package")
                            .toString()
                    req.sign = result.get("sign")
                            .toString()
                    req.extData = "app data" // optional

                    activity.app.api.sendReq(req)
                    return true
                }
                else -> return false
            }
        }
    }
}