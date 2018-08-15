package shy.car.sdk.travel.html

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_register_agreement.*
import kotlinx.android.synthetic.main.layout_title.*
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap.RegisterAgree

/**
 * create by Sharon at 2018/07/02
 * 注册协议
 */
@Route(path = RegisterAgree)
class RegisterAgreeMentActivity : XTBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_agreement)
        txt_title.text =="注册协议"
        webView_register_agreement.settings.domStorageEnabled = true
        webView_register_agreement.loadUrl(app.setting?.htmls?.registerAgreement)
//        webView_register_agreement.loadUrl(BuildConfig.Host + app.InterfaceVersion + "/html/agreement/register.html")
    }
}