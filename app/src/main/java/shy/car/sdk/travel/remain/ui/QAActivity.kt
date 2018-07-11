package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityQaBinding
import shy.car.sdk.travel.remain.presenter.QAPresenter

/**
 * create by LZ at 2018/07/11
 * 常见问题
 */
@Route(path = RouteMap.QA)
class QAActivity : XTBaseActivity() {

    lateinit var binding: ActivityQaBinding
    lateinit var presenter: QAPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qa)
        presenter = QAPresenter(this)

        binding.ac = this
        binding.presenter = presenter

        presenter.getQAList()
    }
}