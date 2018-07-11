package shy.car.sdk.travel.html

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.Phone
import com.base.util.Version
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityAboutBinding

/**
 * create by Sharon at 2018/07/02
 * 注册协议
 */
@Route(path = RouteMap.About)
class AboutActivity : XTBaseActivity() {

    var servicePhone = ObservableField<String>()
    var email = ObservableField<String>()
    var version = ObservableField<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about)
        binding.ac = this
        version.set("v" + Version.getVersion(this))
        servicePhone.set(app.servicePhone)
        email.set(app.setting?.system?.kfEmail)
//        webView_register_agreement.settings.domStorageEnabled = true
//        webView_register_agreement.loadUrl(BuildConfig.Host + "html/about.html")
    }

    fun call() {
        Phone.call(this, servicePhone.get())
    }
}