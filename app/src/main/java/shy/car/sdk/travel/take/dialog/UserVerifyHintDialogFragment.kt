package shy.car.sdk.travel.take.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.ObjectSerialisation
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogMoneyVerifyBinding
import shy.car.sdk.databinding.DialogUserVerifyHintBinding
import shy.car.sdk.travel.take.data.TakeOrderList

/**
 * create by LZ at 2018/05/23
 * 提示未认证
 */
class UserVerifyHintDialogFragment : XTBaseDialogFragment() {
    lateinit var binding: DialogUserVerifyHintBinding
    var takeOrderList: TakeOrderList? = null

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
                .build(RouteMap.OrderTakeDetail)
                .withObject("takeOrderList", takeOrderList)
                .navigation(app)
        dismissAllowingStateLoss()
    }

    fun goVerify() {
        ARouter.getInstance()
                .build(RouteMap.VerifyUser)
                .navigation()
        dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        takeOrderList = null
    }

}