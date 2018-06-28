package shy.car.sdk.travel.user.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogDriveCardBinding
import shy.car.sdk.databinding.DialogIdCardFrontBinding

/**
 * create by LZ at 2018/06/28
 * 身份证
 */
class UserVerifyDriveCardDialog : BottomSheetDialogFragment() {

    interface OnConfirmClick {
        fun onClick()
    }

    var listener: OnConfirmClick? = null

    lateinit var binding: DialogDriveCardBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_drive_card, null, false)
        binding.fragment = this
        return binding.root

    }

    fun onConfirmClick() {
        listener?.onClick()
        dismissAllowingStateLoss()
    }
}