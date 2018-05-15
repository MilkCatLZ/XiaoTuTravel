package shy.car.sdk.travel.order.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderSendBinding

/**
 * create by LZ at 2018/05/11
 */
@Route(path = RouteMap.OrderSend)
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