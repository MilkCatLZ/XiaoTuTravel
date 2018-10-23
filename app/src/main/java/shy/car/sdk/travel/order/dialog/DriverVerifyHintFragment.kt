package shy.car.sdk.travel.order.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogDriveVerifyHintBinding
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/23
 * 提示未认证
 */
class DriverVerifyHintFragment : XTBaseDialogFragment() {
    lateinit var binding: DialogDriveVerifyHintBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_drive_verify_hint, null, false)
        binding.fragment = this
        return binding.root
    }


    fun goDriverVerify() {
        if (!User.instance.getIsDriverAuth()) {
            ARouter.getInstance()
                    .build(RouteMap.DriveVerify)
                    .navigation()
        }
        dismissAllowingStateLoss()
    }

}