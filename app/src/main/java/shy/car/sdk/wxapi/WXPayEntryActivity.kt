package shy.car.sdk.wxapi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.base.util.Log
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.eventbus.PayCancel
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.travel.pay.data.PayMethod


class WXPayEntryActivity : XTBaseActivity(),
        IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAppID, false)
        api?.handleIntent(intent, this)
        register(this)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {
        Log.d("WXPAY------------------", req.openId)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(resp: BaseResp) {

        var result = ""

        result = when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val success = PaySuccess()
                success.payMethod = PayMethod(2, "微信支付", "")
                success.price = info?.price.toString()
                EventBus.getDefault()
                        .post(success)
                "微信支付成功"
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {

                if (BuildConfig.DEBUG) {
                    EventBus.getDefault()
                            .post(PayCancel())
                }
                "微信支付取消"
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> "微信支付被拒绝"
            else -> "微信支付失败"
        }

        Log.e("微信支付-----------------------", result)
        finish()
    }

    private var info: PayInfo? = null

    @Subscribe(sticky = true)
    fun onPayInfo(info: PayInfo) {
        eventBusDefault.removeStickyEvent(info)
        this.info = info
    }
}