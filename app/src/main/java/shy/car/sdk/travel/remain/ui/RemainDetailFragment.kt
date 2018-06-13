package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentRemainDetailBinding

/**
 * create by LZ at 2018/06/13
 * 我的余额
 */
class RemainDetailFragment : XTBaseFragment() {

    lateinit var binding: FragmentRemainDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_remain_detail, null, false)
        binding.fragment = this
        return binding.root

    }
}