package shy.car.sdk.travel.rent.presenter

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.location.Location
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.data.NearCarPoint

class ReturnCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onError(e: Throwable)
        //        fun getListSuccess(t: ArrayList<NearCarPoint>)
        fun returnSuccess(t: JsonObject)

        //        fun getDetailSuccess(t: RentOrderDetail)
        fun getDataSuccess(detail: RentOrderDetail, list: List<NearCarPoint>)

    }

    val location = ObservableField<Location>()
    val agree = ObservableBoolean(true)
    val locationCheckText = ObservableField<String>("")
    var isInNetWork: Boolean = false
    var oid: String = ""
    var nearCarPoint: NearCarPoint? = null

    init {

    }
//
//    fun getNetWork() {
//
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.getNearList(app.location.cityCode)
//        val observer = object : Observer<ArrayList<NearCarPoint>> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: ArrayList<NearCarPoint>) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.getListSuccess(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.onError(e)
//            }
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }

    fun returnCar() {
        if (nearCarPoint == null) {
            ToastManager.showShortToast(context, "请先将车辆开进停车区域的停车位内")
            return
        }
        if (!agree.get()) {
            ToastManager.showLongToast(context, "请仔细阅读契约条款")
            return
        }


        ProgressDialog.showLoadingView(context)


        val observable = ApiManager.getInstance()
                .api.returnCar(oid, nearCarPoint?.id!!, app.location.lat.toString(), app.location.lng.toString())

        val observableShouldTakePhoto = ApiManager.getInstance()
                .api.shoulTakePhoto("2")// 2还车
        observableShouldTakePhoto.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.get("whether").asInt == 1) {
                        ARouter.getInstance()
                                .build(RouteMap.ReturnCarUploadPhoto)
                                .withString(ParamsConstant.String1, oid)
                                .withString(ParamsConstant.String2, nearCarPoint?.id)
                                .navigation()
                    }
                }
                .subscribeOn(Schedulers.io())

                .flatMap {
                    if (it.get("whether").asInt == 0) {
                        observable
                    } else {
                        null
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JsonObject> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: JsonObject) {
                        ProgressDialog.hideLoadingView(context)
                        callBack.returnSuccess(t)
                    }

                    override fun onError(e: Throwable) {

                        ProgressDialog.hideLoadingView(context)
                        if (e is NullPointerException) {

                        } else {
                            ErrorManager.managerError(context, e, "还车失败，请重试")
                        }
                    }
                })


    }

//    fun getRentOrderDetail(orderID: String) {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.getRentOrderDetail(orderID)
//        val observer = object : Observer<RentOrderDetail> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: RentOrderDetail) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.getDetailSuccess(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context, e, "获取订单失败")
//                callBack.onError(e)
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }

    fun getData(orderID: String) {
        val observableDetail = ApiManager.getInstance()
                .api.getRentOrderDetail(orderID)
        val observableNetWork = ApiManager.getInstance()
                .api.getNearList(app.location.cityCode)

        observableDetail.zipWith(observableNetWork, BiFunction<RentOrderDetail, List<NearCarPoint>, Boolean> { detail, list ->
            callBack.getDataSuccess(detail, list)
            true
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({}, { it -> ErrorManager.managerError(context, it, "请求失败") })
    }
}