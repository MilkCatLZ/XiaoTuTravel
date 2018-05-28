package shy.car.sdk.travel.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentSendHoldCarBinding
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.dialog.CommonWheelItem
import shy.car.sdk.travel.send.dialog.SendTimeSelectDialogFragment
import shy.car.sdk.travel.send.presenter.SendHoleCarPresenter


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendHoleCarFragment : XTBaseFragment(), SendHoleCarPresenter.CallBack, SendTimeSelectDialogFragment.OnItemSelectedListener {
    override fun onTimeSelect(date: CommonWheelItem, time: CommonWheelItem) {
        binding.txtUseTime.setText(date.name + "   " + time.name)
    }

    override fun getCarListSuccess(list: ArrayList<CarInfo>) {

    }

    lateinit var binding: FragmentSendHoldCarBinding
    lateinit var presenter: SendHoleCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = SendHoleCarPresenter(it, this) }
        register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_hold_car, null, false)
        binding.fragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager()
    }

    private fun initPager() {
        binding.viewPagerSendHoldCar.adapter = presenter.adapter
        binding.viewPagerSendHoldCar.setPageTransformer(true, TransFormer())
        binding.viewPagerSendHoldCar.offscreenPageLimit = 5
    }

    fun onConfirmClick() {

    }

    fun onSelectAddressClick() {
        ARouter.getInstance()
                .build(RouteMap.LocationSelect)
                .navigation()
    }

    var timeSelectDialogFragment: SendTimeSelectDialogFragment = SendTimeSelectDialogFragment()

    fun openTimeSelect() {
        timeSelectDialogFragment.listener = this
        timeSelectDialogFragment.isCancelable = false
        timeSelectDialogFragment.show(childFragmentManager, "fragment_date_select")
    }


    @Subscribe
    fun onLocationSelect(address: CurrentLocation) {

    }
}