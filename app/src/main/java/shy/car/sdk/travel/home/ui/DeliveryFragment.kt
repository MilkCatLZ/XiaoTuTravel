package shy.car.sdk.travel.home.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.layout_home_top.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentDeliveryBinding

class DeliveryFragment : XTBaseFragment() {
    lateinit var binding: FragmentDeliveryBinding
    private val orderTakeFragment = ARouter.getInstance().build(RouteMap.OrderTake).navigation() as Fragment
    private val orderSendFragment = ARouter.getInstance().build(RouteMap.OrderSend).navigation() as Fragment

    var checkID = ObservableInt(0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery, null, false)
        binding.fragment = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPageFragment()
    }


    private fun initPageFragment() {


        var transaction = childFragmentManager.beginTransaction()


        transaction.add(R.id.frame_fragment_content, orderTakeFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderSendFragment, tag)

        transaction.hide(orderSendFragment)
//        transaction.hide(orderTakeFragment)
        transaction.commit()

        binding.radioTake.performClick()
    }

    fun onClick(type: Int) {
        when (type) {
            0 -> {
                changeFragment(orderTakeFragment)
            }
            1 -> {
                changeFragment(orderSendFragment)
            }
        }
        checkID.set(type)
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.hide(orderTakeFragment)
        transaction.hide(orderSendFragment)
        transaction.show(fragment)
        transaction.commit()
    }

}