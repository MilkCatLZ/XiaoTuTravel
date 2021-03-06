package shy.car.sdk.travel.rent.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogRentCarNoPayBinding

class RentNoPayDialog : XTBaseDialogFragment() {

    lateinit var binding: DialogRentCarNoPayBinding
    var oid=""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rent_car_no_pay, null, false)
        binding.dialog = this
        return binding.root

    }


    fun goPay(){
        ARouter.getInstance()
                .build(RouteMap.OrderPay)
                .withString(ParamsConstant.String1, oid)
                .navigation()
        dismissAllowingStateLoss()
    }
}