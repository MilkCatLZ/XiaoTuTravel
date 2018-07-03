package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Log
import com.base.util.Phone
import com.base.util.StringUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.route.RouteMap.ReturnCar
import shy.car.sdk.databinding.FragmentDrivingBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.dialog.LockCarDialogFragment
import shy.car.sdk.travel.rent.dialog.OpenCarDialogFragment
import shy.car.sdk.travel.rent.dialog.RingCarDialogFragment
import shy.car.sdk.travel.rent.presenter.DrivingPresenter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * create by 过期猫粮 at 2018/06/24
 * 行程中
 */
class DrivingFragment : XTBaseFragment(),
        DrivingPresenter.CallBack {
    override fun onGetDetailSuccess(t: RentOrderDetail) {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onGetDetailError(e: Throwable) {

    }

    fun setOid(oid: String) {
        presenter.oid = oid
    }

    lateinit var binding: FragmentDrivingBinding
    lateinit var presenter: DrivingPresenter

    var isRunningRefreshing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = DrivingPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driving, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefresh.setOnRefreshListener {
            presenter.getOrderDetail(presenter.oid)
        }
        startAutoRefresh()
    }

    var dispose: Disposable? = null
    private fun startAutoRefresh() {
        if (!isRunningRefreshing) {
            isRunningRefreshing = true
            Observable.create<String> {
                while (StringUtils.isEmpty(presenter.oid)) {
                    try {
                        Thread.sleep(100)
                    } catch (e: Exception) {
                    }
                }
                it.onNext(presenter.oid)
            }
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        Observable.interval(0, 30, TimeUnit.SECONDS)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                            dispose = d
                        }

                        override fun onNext(t: Long) {
                            presenter.getOrderDetail(presenter.oid)
                            Log.d("asdfsdasdfsa", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Calendar.getInstance().time))

                        }

                        override fun onError(e: Throwable) {

                        }

                    })
        }
    }

    /**
     * 联系调度员
     */
    fun callAdmin() {
        activity?.let {
            Phone.call(it, presenter.detail.get()?.dispatchPhone)
        }
    }

    /**
     * 上报故障
     */
    fun feedBackTrouble() {
        ARouter.getInstance()
                .build(RouteMap.CarBrokeUpLoad)
                .navigation()
    }

    /**
     * 鸣笛
     */
    fun ringCar() {
        var ring = RingCarDialogFragment()
        ring.carid = presenter.detail.get()?.car?.carID!!
        ring.show(childFragmentManager, "fragment_ring")
    }

    /**
     * 还车
     */
    fun gotoReturn() {
        ARouter.getInstance()
                .build(ReturnCar)
                .withString(String1, presenter.detail.get()?.orderId)
                .navigation()
    }

    /**
     * 开启车门
     */
    fun openDoor() {
        var open = OpenCarDialogFragment()
        open.carID = presenter.detail.get()?.car?.carID!!
        open.oid = presenter.detail.get()?.orderId!!
        open.show(childFragmentManager, "fragment_open")
    }

    /**
     * 关闭车门
     */
    fun lockDoor() {
        var open = LockCarDialogFragment()
        open.carID = presenter.detail.get()?.car?.carID!!
        open.oid = presenter.detail.get()?.orderId!!
        open.show(childFragmentManager, "fragment_lock")
    }

    fun gotoReturnCarArea() {
        ARouter.getInstance()
                .build(RouteMap.ReturnArea)
                .navigation()
    }

    override fun onDestroy() {
        dispose?.dispose()
        isRunningRefreshing = false
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        dispose?.dispose()
        isRunningRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        startAutoRefresh()
    }
}