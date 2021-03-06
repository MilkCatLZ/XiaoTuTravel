package shy.car.sdk.travel.user.ui

import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogUserVerifySubmitSuccessBinding
import shy.car.sdk.travel.user.data.User

/**
 * create by lz at 2018/06/04
 * 提交成功
 */
class UserVerifySubmitSuccessDialogFragment : XTBaseDialogFragment() {


    var listener: DialogInterface.OnDismissListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding: DialogUserVerifySubmitSuccessBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_verify_submit_success, null, false)
        binding.onClick = View.OnClickListener {
            if (!User.instance.getIsDeposit()) {
                ARouter.getInstance()
                        .build(RouteMap.MoneyVerify)
                        .navigation()
            }
            dismissAllowingStateLoss()
        }
        return binding.root

    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.onDismiss(null)
    }
}