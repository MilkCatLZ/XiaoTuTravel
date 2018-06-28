package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.base.BaseFragment
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentPromiseMoneyReturnBinding

class PromiseMoneyReturnFragment : XTBaseFragment() {

    lateinit var binding: FragmentPromiseMoneyReturnBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_promise_money_return, null, false)
        return binding.root

    }
}