package shy.car.sdk.travel.send.ui

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.ToastManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.PageManager
import shy.car.sdk.databinding.FragmentSendCitySmallPackageBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.data.CarUserTime
import shy.car.sdk.travel.send.presenter.SendCitySmallPackagePresenter


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendCitySmallPackageFragment : XTBaseFragment(),
        SendCitySmallPackagePresenter.CallBack {

    override fun onSubmitSuccess() {

        activity?.let {
            ToastManager.showShortToast(it, "发布成功")
        }
        RefreshOrderList.refreshOrderList()
        finish()
    }

    override fun onSubmitError() {

    }

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
        presenter.getData()
    }

    fun onConfirmClick() {
        presenter.submit()
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

    fun openTimeSelect() {
        activity?.let {
            PageManager.delayStartFragmentDialog<List<CarUserTime>>(it, ApiManager.getInstance().api.getCarUseTime(), object : PageManager.BeforeNavigateListener<List<CarUserTime>> {
                override fun beforeNavigate(dialog: Any, t: List<CarUserTime>) {
                    if (dialog is SendTimeSelectDialogFragment) {
                        dialog.listener =
                                object : SendTimeSelectDialogFragment.OnItemSelectedListener {
                                    @SuppressLint("SetTextI18n")
                                    override fun onTimeSelect(date: CommonWheelItem, time: CommonWheelItem) {
                                        binding.txtUseTime.text = "${date.name}     ${time.name}"
                                        if (time.name.contains("-")) {
                                            var arr = time.name.split("-")
                                            presenter.startTime = arr[0].trim()
                                            presenter.endTime = arr[1].trim()
                                        } else {
                                            presenter.startTime = date.name + " 00:00:00"
                                            presenter.endTime = "0"
                                        }
                                    }

                                }
                        dialog.setList(t)
                        dialog.isCancelable = false
                    }
                }

            }, RouteMap.SendTimeSelect, childFragmentManager.beginTransaction(), "dialog_time_select")
        }
    }

    val goodsDialog: GoodsTypeSelectDialogFragment = GoodsTypeSelectDialogFragment()
    fun onSelectGoodsTypeClick() {
        goodsDialog.listener = object : GoodsTypeSelectDialogFragment.OnItemSelectedListener {
            override fun onTimeSelect(goodsType: GoodsType) {
                presenter.goodsType.set(goodsType)
            }

        }
        goodsDialog.isCancelable = false
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

    fun gotoAgreeMent() {
        ARouter.getInstance()
                .build(RouteMap.SendAgreeMent)
                .navigation()
    }
}