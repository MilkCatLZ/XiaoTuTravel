package shy.car.sdk.travel.setting.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/05/16
 * 客服
 */
@Route(path = RouteMap.KeFu)
class KeFuActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kefu)
    }
}