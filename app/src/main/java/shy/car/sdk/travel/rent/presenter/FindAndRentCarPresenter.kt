package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.eventbus.RentOrderCanceled
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.order.net.OrderManager

/**
 * 找车取车
 */
class FindAndRentCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var oid: String = ""
    lateinit var detail: RentOrderDetail

    interface CallBack {
        fun onRingError(e: Throwable)
        fun onCancelSuccess()
        fun onCancelError(e: Throwable)
        fun onGetDetailError(e: Throwable)
        fun onUnLockSuccess()
        fun onGetDetailSuccess(t: RentOrderDetail)
    }

//    fun carRing() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.carAction(detail.car?.carID!!, status = 1.toString())
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: JsonObject) {
//                ToastManager.showLongToast(context, "车辆已鸣笛，请尽快找到车辆")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//            override fun onError(e: Throwable) {
//                ErrorManager.managerError(context, e, "操作失败，请重试")
//                ProgressDialog.hideLoadingView(context)
//                callBack.onRingError(e)
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }

    fun unLockCar() {

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()

        val observableShouldTakePhoto = ApiManager.getInstance()
                .api.shoulTakePhoto("1")// 1租车

        val observableUnLock = ApiManager.getInstance()
                //固定传3
                .api.orderUnLockCarAndStart(detail.orderId!!/*, image = null*/)


        observableShouldTakePhoto.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.get("whether").asInt == 1) {
                        ARouter.getInstance()
                                .build(RouteMap.UnLockCar)
                                .withString(String1, detail.orderId)
                                .navigation()
                    }
                }
                .subscribeOn(Schedulers.io())
                .flatMap {
                    if (it.get("whether").asInt == 0) {
                        observableUnLock
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
                        ToastManager.showLongToast(context, "车辆已解锁，请尽快上车")
                        ProgressDialog.hideLoadingView(context)
                        callBack.onUnLockSuccess()
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialog.hideLoadingView(context)
                        if (e is NullPointerException) {

                        } else {
                            ErrorManager.managerError(context, e, "操作失败，请重试")
                            callBack.onRingError(e)
                        }
                    }

                })

    }

    fun cancelOrder() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        ApiManager.getInstance()
                .api.cancelRentOrder(detail.orderId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelSuccess()
                    ApiManager.getInstance()
                            .clearCache()
                    RefreshOrderList.refreshOrderList()
                    EventBus.getDefault()
                            .post(RentOrderCanceled())
                }, {
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelError(it)
                    it.printStackTrace()
                })
    }


    fun getOrderDetail() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        OrderManager.getOrderDetail(oid, true, callBack = object : OrderManager.GetDetailCallBack {
            override fun getDetailSuccess(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                detail = t
                callBack.onGetDetailSuccess(t)
            }

            override fun getDetailError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailError(e)
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

        })
    }
}