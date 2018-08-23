package shy.car.sdk.travel.message.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMessageCenterBinding
import shy.car.sdk.travel.message.data.MessageInfo

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
        getMessage()
    }

    private fun getMessage() {
        val observable = ApiManager.getInstance()
                .api.getMessageInfo(0)
        val observer = object : Observer<MessageInfo> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: MessageInfo) {
                binding.info = t
            }

            override fun onError(e: Throwable) {

            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun onMessageActiveClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageActive)
                .navigation()
    }

    fun onMessageServiceClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageService)
                .navigation()
    }

    fun onMessageSystemClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageSystem)
                .navigation()
    }
}