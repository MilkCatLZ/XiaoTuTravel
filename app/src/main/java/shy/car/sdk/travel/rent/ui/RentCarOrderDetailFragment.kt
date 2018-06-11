package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.util.Phone
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentRentCarOrderDetailBinding

/**
 * create by LZ at 2018/06/11
 * 租车详情
 */
class RentCarOrderDetailFragment : XTBaseFragment() {

    lateinit var binding: FragmentRentCarOrderDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_car_order_detail, null, false)
        binding.fragment = this
        return binding.root

    }


    fun call() {
        activity?.let { Phone.call(it, "400-056-5317") }
    }

}