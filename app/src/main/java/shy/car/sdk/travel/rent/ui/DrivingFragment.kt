package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentDrivingBinding

/**
 * create by 过期猫粮 at 2018/06/24
 * 行程中
 */
class DrivingFragment : XTBaseFragment() {
    fun setOid(oid: String) {

    }

    lateinit var binding: FragmentDrivingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driving, null, false);
        return binding.root

    }
}