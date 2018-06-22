package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityTixianBinding


@Route(path = RouteMap.TiXian)
class TiXianActivity : XTBaseActivity() {
    lateinit var binging: ActivityTixianBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binging = DataBindingUtil.setContentView(this, R.layout.activity_tixian)
        binging.ac = this

    }
}