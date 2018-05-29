package shy.car.sdk.travel.location.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/05/28
 * 选择地址
 */
@Route(path = RouteMap.LocationSelect)
class LocationSelectActivity:XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_select)
    }
}