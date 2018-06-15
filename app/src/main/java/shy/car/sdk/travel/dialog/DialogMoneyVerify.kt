package shy.car.sdk.travel.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogMoneyVerifyBinding

/**
 *
 */
@Route(path = RouteMap.Dialog_Money_Verify)
class DialogMoneyVerify : XTBaseDialogFragment() {
    lateinit var binding: DialogMoneyVerifyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_money_verify, null, false)
        binding.fragment = this
        return binding.root
    }

    fun onConfirmClick() {
        ARouter.getInstance()
                .build(RouteMap.MoneyVerify)
                .navigation()
        dismiss()
    }


}