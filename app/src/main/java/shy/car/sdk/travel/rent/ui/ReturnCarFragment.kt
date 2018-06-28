package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.PolygonOptions
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.data.RefreshRentCarState
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentReturnCarBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.presenter.ReturnCarPresenter

/**
 * create by LZ at 2018/06/25
 * 还车
 */
class ReturnCarFragment : XTBaseFragment(),
        ReturnCarPresenter.CallBack {
    override fun getDataSuccess(detail: RentOrderDetail, list: List<NearCarPoint>) {
        Observable.create<NearCarPoint> { emiter ->
            lateinit var near: NearCarPoint
            var isInNetWork = false
            binding.map.map.clear()
            list.map {
                val pOption = PolygonOptions()
                it.range?.map {

                    pOption.add(LatLng(it.lat, it.lng))
                }
                val polygon = binding.map.map.addPolygon(pOption.strokeWidth(4f)
                        .strokeColor(Color.argb(50, 1, 1, 1))
                        .fillColor(Color.argb(50, 1, 1, 1)))
                if (polygon.contains(LatLng(detail.car?.lat!!, detail.car?.lng!!))) {
                    near = it
                    isInNetWork = true
                }
            }
            if (isInNetWork) {
                emiter.onNext(near)
                emiter.onComplete()
            } else {
                emiter.onError(Exception())
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<NearCarPoint> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        dis = d
                    }

                    override fun onNext(t: NearCarPoint) {
                        presenter.locationCheckText.set(t.address)
                        presenter.isInNetWork = true
                        presenter.nearCarPoint = t
                        binding.imgLocation.setImageResource(R.drawable.img_site)
                        binding.txtAddress.setTextColor(resources.getColor(R.color.text_primary_333333))
                    }

                    override fun onError(e: Throwable) {
                        presenter.locationCheckText.set("未到小兔出行科技停放点")
                        presenter.isInNetWork = false
                        binding.imgLocation.setImageResource(R.drawable.img_not_site)
                        binding.txtAddress.setTextColor(resources.getColor(R.color.main_color_red))
                    }
                })
    }

    override fun returnSuccess(t: JsonObject) {
        activity?.let {
            ToastManager.showShortToast(it, "还车成功")
        }
        EventBus.getDefault()
                .post(RefreshOrderList())
        EventBus.getDefault()
                .post(RefreshRentCarState(presenter.oid))
        app.goHome()
        finish()
    }

    override fun onError(e: Throwable) {

    }

    var dis: Disposable? = null
//    override fun getListSuccess(t: ArrayList<NearCarPoint>) {


//        Observable.create<NearCarPoint> { emiter ->
//            lateinit var near: NearCarPoint
//            var isInNetWork = false
//            binding.map.map.clear()
//            t.map {
//                val pOption = PolygonOptions()
//                it.range?.map {
//
//                    pOption.add(LatLng(it.lat, it.lng))
//                }
//                val polygon = binding.map.map.addPolygon(pOption.strokeWidth(4f)
//                        .strokeColor(Color.argb(50, 1, 1, 1))
//                        .fillColor(Color.argb(50, 1, 1, 1)))
//                if (polygon.contains(LatLng(app.location.lat, app.location.lng))) {
//                    near = it
//                    isInNetWork = true
//                }
//            }
//            if (isInNetWork) {
//                emiter.onNext(near)
//                emiter.onComplete()
//            } else {
//                emiter.onError(Exception())
//            }
//        }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<NearCarPoint> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        dis = d
//                    }
//
//                    override fun onNext(t: NearCarPoint) {
//                        presenter.locationCheckText.set(t.address)
//                        presenter.isInNetWork = true
//                        presenter.nearCarPoint = t
//                        binding.imgLocation.setImageResource(R.drawable.img_site)
//                        binding.txtAddress.setTextColor(resources.getColor(R.color.text_primary_333333))
//                    }
//
//                    override fun onError(e: Throwable) {
//                        presenter.locationCheckText.set("未到小兔出行科技停放点")
//                        presenter.isInNetWork = false
//                        binding.imgLocation.setImageResource(R.drawable.img_not_site)
//                        binding.txtAddress.setTextColor(resources.getColor(R.color.main_color_red))
//                    }
//                })
//    }

    lateinit var binding: FragmentReturnCarBinding
    lateinit var presenter: ReturnCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            presenter = ReturnCarPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_return_car, null, false)
        binding.fragment = this
        binding.presenter = presenter
        binding.map.onCreate(savedInstanceState)
        binding.map.map.uiSettings.isZoomControlsEnabled = false
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            ProgressDialog.showLoadingView(it)
        }
//        AmapLocationManager.getInstance()
//                .getLocation(object : AmapOnLocationReceiveListener {
//                    override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
//                        app.location.copy(location)
//                        presenter.location.set(location)
//
//                    }
//                })
    }

    fun lockAndReturn() {
        presenter.returnCar()
    }

    override fun onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.map.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.map.onSaveInstanceState(outState)
    }

    fun setOid(order: String) {
        presenter.oid = order
        presenter.getData(order)
    }

    fun gotoReturnArea() {
        ARouter.getInstance()
                .build(RouteMap.ReturnArea)
                .navigation()
    }

}