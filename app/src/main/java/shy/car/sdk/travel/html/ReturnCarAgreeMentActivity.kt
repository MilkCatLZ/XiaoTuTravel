package shy.car.sdk.travel.html

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_return_car_agreement.*
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by Sharon at 2018/07/02
 * 还车协议
 */
@Route(path = RouteMap.ReturnCarAgree)
class ReturnCarAgreeMentActivity : XTBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_car_agreement)
        txt_title.text = "注册协议"
        webView_register_agreement.settings.domStorageEnabled = true
//        webView_register_agreement.loadUrl(BuildConfig.Host + app.InterfaceVersion + "/html/agreement/return_car.html")
        webView_register_agreement.loadUrl(app.setting?.htmls?.returnCarAgreement)
    }
}