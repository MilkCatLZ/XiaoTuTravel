package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.Phone
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityInsuranceProcessBinding
import shy.car.sdk.databinding.FragmentRentOrderCommentBinding

/**
 * create by LZ at 2018/07/04
 * 保险流程
 */
@Route(path = RouteMap.InsuranceProcess)
class InsuranceProcessActivity : XTBaseActivity() {


    lateinit var binding: ActivityInsuranceProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_process)
        binding.ac = this
    }

    fun call() {
        Phone.call(this, app.servicePhone)
    }
}