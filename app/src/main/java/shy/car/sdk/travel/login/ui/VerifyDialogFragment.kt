package shy.car.sdk.travel.login.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.StringUtils
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentVerifyBinding

@Route(path = RouteMap.Verify)
class VerifyDialogFragment : XTBaseDialogFragment() {

    var currentVerifyNum = ObservableInt(0)

    var textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            var t = s.toString()
            if (StringUtils.isNotEmpty(t)) {
                when (currentVerifyNum.get()) {
                    0 -> {
                        currentVerifyNum.set(1)
                        binding.edtVerify1.requestFocus()
                    }

                    1 -> {
                        currentVerifyNum.set(2)
                        binding.edtVerify2.requestFocus()
                    }
                    2 -> {
                        currentVerifyNum.set(3)
                        binding.edtVerify3.requestFocus()
                    }
                    3 -> lockAndSubmit()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    fun touchEvent() {
        when (currentVerifyNum.get()) {
            0 -> binding.edtVerify0.requestFocus()
            1 -> binding.edtVerify1.requestFocus()
            2 -> binding.edtVerify2.requestFocus()
            3 -> binding.edtVerify3.requestFocus()
        }
    }

    /**
     * 提交验证码
     */
    private fun lockAndSubmit() {

    }

    lateinit var binding: FragmentVerifyBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify, null, false)
        binding.fragment = this
        return binding.root
    }

    fun back() {

    }

}