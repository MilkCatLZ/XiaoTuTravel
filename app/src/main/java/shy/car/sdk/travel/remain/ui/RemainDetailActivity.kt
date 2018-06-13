package shy.car.sdk.travel.remain.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/06/13
 * 我的余额
 */
@Route(path = RouteMap.RemainDetail)
class RemainDetailActivity :XTBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remain_detail)
    }
}