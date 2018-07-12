package shy.car.sdk.travel.html

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_recharge_agreement.*
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap.RegisterAgree

/**
 * create by Sharon at 2018/07/02
 * 充值协议
 */
@Route(path = RegisterAgree)
class RechargeAgreeMentActivity : XTBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_agreement)
        webView_register_agreement.settings.domStorageEnabled = true
        webView_register_agreement.loadUrl(app.setting?.htmls?.chargeAgreement)
//        webView_register_agreement.loadUrl(BuildConfig.Host + app.InterfaceVersion + "/html/agreement/charge.html")
    }
}