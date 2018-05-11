package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentOrderSendBinding

/**
 * create by LZ at 2018/05/11
 */
class OrderSendFragment : XTBaseFragment() {

    override fun getFragmentName(): CharSequence {
        return "接单"
    }

    lateinit var binding: FragmentOrderSendBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_send, null, false)
        return binding.root
    }


}