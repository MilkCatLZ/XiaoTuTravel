package shy.car.sdk.travel.send.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivitySendHoldCarBinding

/**
 * create by LZ at 2018/05/25
 */
@Route(path = RouteMap.SendHoleCarEdit)
class SendHoleCarActivity : XTBaseActivity() {
    lateinit var binding: ActivitySendHoldCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_hold_car)
    }

}