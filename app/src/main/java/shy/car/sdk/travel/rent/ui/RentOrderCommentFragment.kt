package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentRentOrderCommentBinding


/**
 * create by LZ at 2018/07/04
 * 租车评价
 */
class RentOrderCommentFragment : XTBaseFragment() {

    lateinit var binding: FragmentRentOrderCommentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_order_comment, null, false)
        return binding.root

    }

}