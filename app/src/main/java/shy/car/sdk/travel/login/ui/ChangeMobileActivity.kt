package shy.car.sdk.travel.login.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/07/03
 * 修改手机号
 */
@Route(path = RouteMap.ChangeMobile)
class ChangeMobileActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_mobile)
    }
}