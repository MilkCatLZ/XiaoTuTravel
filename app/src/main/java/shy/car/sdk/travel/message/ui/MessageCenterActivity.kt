package shy.car.sdk.travel.message.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMessageCenterBinding

/**
 * create by LZ at 2018/05/16
 * 消息中心
 */
@Route(path = RouteMap.MessageCenter)
class MessageCenterActivity : XTBaseActivity() {
    lateinit var binding: ActivityMessageCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_center)
        binding.ac = this
    }

    fun onMessageActiveClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageActive)
                .navigation()
    }
}