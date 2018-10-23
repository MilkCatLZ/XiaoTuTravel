package shy.car.sdk.travel.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogMoneyVerifyBinding
import shy.car.sdk.travel.user.data.User

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
        if (!User.instance.getIsIdentityAuth()) {
            ARouter.getInstance()
                    .build(RouteMap.UserVerify)
                    .navigation()
        } else if (!User.instance.getIsDeposit()) {
            ARouter.getInstance()
                    .build(RouteMap.MoneyVerify)
                    .navigation()
        }
        dismiss()
    }


}