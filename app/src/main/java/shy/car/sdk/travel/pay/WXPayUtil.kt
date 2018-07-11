package shy.car.sdk.travel.pay

import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Log
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
import shy.car.sdk.wxapi.PayInfo

class WXPayUtil {
    companion object {
        /**
         * @return true:第三方支付  false:余额支付
         */
        fun pay(activity: XTBaseActivity, payMethod: PayMethod, price: Double, results: JsonObject): Boolean {
            when {
                payMethod.id == 1 -> {
                    Alipay.getInstance()
                            .pay(activity, results.get("pay_info").asString, object : Alipay.OnPayCallBack {
                                override fun onPaySuccess(payResult: PayResult?) {
                                    val success = PaySuccess()
                                    success.payMethod = PayMethod(1, "支付宝支付", "")
                                    success.price = payResult?.result?.alipayTradeAppPayResponse?.totalAmount
                                    EventBus.getDefault()
                                            .post(success)
                                }

                                override fun onPayFailed(payResult: PayResult?) {
                                    Log.d("alipay-onPayFailed", payResult?.resultStatus + payResult?.result)
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
                payMethod.id == 2 -> {

                    //这里的目的是给WXPayEntryActivity传递参数，获取价格，还未测试
                    val info = PayInfo()
                    info.price = price
                    EventBus.getDefault()
                            .postSticky(info)

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