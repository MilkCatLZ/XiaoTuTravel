package shy.car.sdk.travel.message.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/05/16
 * 系统消息
 */
@Route(path = RouteMap.MessageSystem)
class MessageSystemActivity :XTBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_system)
    }
}