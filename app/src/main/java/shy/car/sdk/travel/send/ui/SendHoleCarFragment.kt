package shy.car.sdk.travel.send.ui

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentSendHoldCarBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.presenter.SendHoleCarPresenter
import java.util.*


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendHoleCarFragment : XTBaseFragment(),
        SendHoleCarPresenter.CallBack {

    lateinit var binding: FragmentSendHoldCarBinding
    lateinit var presenter: SendHoleCarPresenter

    private var isStart = false
    private var isEnd = false


    override fun getCarListSuccess(list: ArrayList<CarInfo>) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = SendHoleCarPresenter(it, this) }
        register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_hold_car, null, false)
        binding.fragment = this
        binding.presenter = presenter
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

    fun onSelectStartLocationclick() {
        ARouter.getInstance()
                .build(RouteMap.LocationSelect)
                .navigation()
        isStart = true
        isEnd = false
    }


    fun onSelectEndLocationClick() {
        ARouter.getInstance()
                .build(RouteMap.LocationSelect)
                .navigation()
        isStart = false
        isEnd = true
    }

    var timeSelectDialogFragment: SendTimeSelectDialogFragment = SendTimeSelectDialogFragment()

    fun openTimeSelect() {
        timeSelectDialogFragment.listener = object : SendTimeSelectDialogFragment.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onTimeSelect(date: CommonWheelItem, time: CommonWheelItem) {
                binding.txtUseTime.text = "${date.name}     ${time.name}"
                if ("全天" == time.name) {
                    presenter.startTime = date.name + " 00:00:00"
                    presenter.endTime = "0"
                } else {
                    presenter.startTime = date.name + " 18:00:00"
                    presenter.startTime = date.name + " 23:59:59"
                }
            }

        }
        timeSelectDialogFragment.isCancelable = false
        timeSelectDialogFragment.show(childFragmentManager, "fragment_date_select")
    }

    fun onSelectGoodsTypeClick() {
        val goodsDialog = ARouter.getInstance().build(RouteMap.GoodsTypeSelect).navigation() as GoodsTypeSelectDialogFragment
        goodsDialog.listener = object : GoodsTypeSelectDialogFragment.OnItemSelectedListener {
            override fun onTimeSelect(goodsType: GoodsType) {
                presenter.goodsType.set(goodsType)
            }

        }
        goodsDialog.isCancelable
        goodsDialog.show(childFragmentManager, "dialog_select_goods_type")
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationSelect(address: CurrentLocation) {
        if (isStart) {
            presenter.startLocation.set(address)
        } else if (isEnd) {
            presenter.endLocation.set(address)
        }
        isStart = false
        isEnd = false

    }
}