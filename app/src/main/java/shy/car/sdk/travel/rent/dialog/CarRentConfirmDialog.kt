package shy.car.sdk.travel.rent.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogRentConfirmBinding
import shy.car.sdk.travel.rent.data.CarInfo

class CarRentConfirmDialog : XTBaseDialogFragment() {
    var listener: OnConfirmListener? = null

    interface OnConfirmListener {
        fun confirm()
    }

    var carinfo: CarInfo? = null
    lateinit var binding: DialogRentConfirmBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rent_confirm, null, false)
        binding.carInfo = carinfo
        binding.fragment = this
        return binding.root
    }

    fun onConfirmClick() {
        listener?.confirm()
        dismissAllowingStateLoss()
    }
}