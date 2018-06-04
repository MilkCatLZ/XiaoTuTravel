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
import shy.car.sdk.databinding.FragmentSendCitySmallPackageBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.presenter.SendCitySmallPackagePresenter


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendCitySmallPackageFragment : XTBaseFragment(), SendCitySmallPackagePresenter.CallBack {

    lateinit var binding: FragmentSendCitySmallPackageBinding
    lateinit var presenter: shy.car.sdk.travel.send.presenter.SendCitySmallPackagePresenter

    private var isStart = false
    private var isEnd = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = SendCitySmallPackagePresenter(it, this) }
        register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_city_small_package, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        goodsDialog.show(childFragmentManager,"dialog_select_goods_type")
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