package shy.car.sdk.travel.user.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogUserVerifySubmitSuccessBinding

/**
 * create by lz at 2018/06/04
 * 提交成功
 */
class UserVerifySubmitSuccessDialogFragment : XTBaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding: DialogUserVerifySubmitSuccessBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_verify_submit_success, null, false)
        binding.onClick = View.OnClickListener {
            dismiss()
        }
        return binding.root

    }

}