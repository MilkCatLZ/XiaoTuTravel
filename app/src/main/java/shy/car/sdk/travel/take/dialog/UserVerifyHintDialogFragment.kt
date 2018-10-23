package shy.car.sdk.travel.take.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogUserVerifyHintBinding
import shy.car.sdk.travel.take.data.DeliveryOrderList

/**
 * create by LZ at 2018/05/23
 * 提示未认证
 */
class UserVerifyHintDialogFragment : XTBaseDialogFragment() {
    lateinit var binding: DialogUserVerifyHintBinding
    var takeOrderList: DeliveryOrderList? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_verify_hint, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun goDetail() {
        ARouter.getInstance()
                .build(RouteMap.OrderDetail)
                .withString(String1, takeOrderList?.freightId)
                .navigation(app)
        dismissAllowingStateLoss()
    }

    fun goVerify() {
        ARouter.getInstance()
                .build(RouteMap.UserVerify)
                .navigation()
        dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        takeOrderList = null
    }

}