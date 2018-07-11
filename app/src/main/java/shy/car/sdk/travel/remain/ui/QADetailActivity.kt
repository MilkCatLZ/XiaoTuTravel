package shy.car.sdk.travel.remain.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_recharge_agreement.*
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.route.RouteMap.RegisterAgree

/**
 * create by Sharon at 2018/07/02
 * 充值协议
 */
@Route(path = RouteMap.QADetail)
class QADetailActivity : XTBaseActivity() {

    @Autowired(name = String1)
    @JvmField
    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        setContentView(R.layout.activity_recharge_agreement)
        webView_register_agreement.settings.domStorageEnabled = true
        webView_register_agreement.loadUrl(url)
    }
}