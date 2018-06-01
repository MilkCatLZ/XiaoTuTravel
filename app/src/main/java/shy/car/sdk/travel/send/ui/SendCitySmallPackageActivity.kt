package shy.car.sdk.travel.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivitySendCityPackageBinding

/**
 * create by LZ at 2018/05/25
 * 同城小包
 */
@Route(path = RouteMap.SendCitySmallPackageSelect)
class SendCitySmallPackageActivity : XTBaseActivity() {
    lateinit var binding: ActivitySendCityPackageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_city_package)
    }

}