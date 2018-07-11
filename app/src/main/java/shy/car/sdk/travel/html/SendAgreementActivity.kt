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
 * 注册协议
 */
@Route(path = RouteMap.SendAgreeMent)
class SendAgreementActivity : XTBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_car_agreement)
        webView_register_agreement.settings.domStorageEnabled = true
        webView_register_agreement.loadUrl(BuildConfig.Host + app.InterfaceVersion + "/html/agreement/freight.html")
    }
}