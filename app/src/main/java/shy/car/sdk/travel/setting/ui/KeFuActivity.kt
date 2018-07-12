package shy.car.sdk.travel.setting.ui

import android.databinding.DataBindingUtil
import android.os.Binder
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.Phone
import com.base.util.ToastManager
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityKefuBinding
import shy.car.sdk.travel.remain.presenter.QAPresenter

/**
 * create by LZ at 2018/05/16
 * 客服
 */
@Route(path = RouteMap.KeFu)
class KeFuActivity : XTBaseActivity() {

    lateinit var binding: ActivityKefuBinding
    lateinit var presenter: QAPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kefu)
        presenter = QAPresenter(this)

        binding.ac = this
        binding.presenter = presenter
        presenter.getQAList()
    }

    fun onlineService() {
        ToastManager.showShortToast(this, "功能开发中")
    }

    fun onlineServicePhone() {
        Phone.call(this, app.servicePhone)
    }
}