package shy.car.sdk.travel.send.ui

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.ToastManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.PageManager
import shy.car.sdk.databinding.FragmentSendHoldCarBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.data.CarUserTime
import shy.car.sdk.travel.send.presenter.SendHoleCarPresenter
import java.util.*


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendHoleCarFragment : XTBaseFragment(),
        SendHoleCarPresenter.CallBack {

    override fun onSubmitSuccess() {
        ToastManager.showShortToast(activity, "发布成功")
        finish()
    }

    override fun onSubmitError() {

    }

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
        presenter.getData()
    }

    private fun initPager() {
        binding.viewPagerSendHoldCar.adapter = presenter.adapter
        binding.viewPagerSendHoldCar.setPageTransformer(true, TransFormer())
        binding.viewPagerSendHoldCar.offscreenPageLimit = 5
        binding.viewPagerSendHoldCar.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                presenter.carID = presenter.adapter.items[position].id.toString()
            }
        })
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

    //    var timeSelectDialogFragment: SendTimeSelectDialogFragment = SendTimeSelectDialogFragment()
    private val goodsDialog = ARouter.getInstance().build(RouteMap.GoodsTypeSelect).navigation() as GoodsTypeSelectDialogFragment

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

    fun onSelectGoodsTypeClick() {


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

    fun gotoAgreeMent() {
        ARouter.getInstance()
                .build(RouteMap.SendAgreeMent)
                .navigation()
    }
}


