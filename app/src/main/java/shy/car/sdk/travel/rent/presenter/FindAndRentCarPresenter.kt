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
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.OrderMineList

/**
 * 找车取车
 */
class FindAndRentCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    lateinit var detail: OrderMineList

    interface CallBack {
        fun onRingError(e: Throwable)
        fun onCancelSuccess()
        fun onCancelError(e: Throwable)
        fun onGetDetailError(e: Throwable)
    }

    fun carRing() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.carAction(detail.car?.freightId!!, status = 1.toString())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ToastManager.showLongToast(context, "车辆已鸣笛，请尽快找到车辆")
                ProgressDialog.hideLoadingView(context)
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, "操作失败，请重试")
                ProgressDialog.hideLoadingView(context)
                callBack.onRingError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun unLockCar() {

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observableUnLock = ApiManager.getInstance()
                //固定传3
                .api.orderUnLockCarAndStart(detail.id)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ToastManager.showLongToast(context, "车辆已解锁，请尽快上车")
                ProgressDialog.hideLoadingView(context)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                val err = ErrorManager.managerError(context, e, "操作失败，请重试")
                if (err?.error_code == 400404) {
                    ARouter.getInstance()
                            .build(RouteMap.UnLockCar)
                            .withString(ParamsConstant.String1, detail.id)
                            .navigation()

                }else{
                    err?.showError(context,"解锁失败+")
                }
                callBack.onRingError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observableUnLock, observer)
    }

    fun cancelOrder() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        ApiManager.getInstance()
                .api.cancelRentOrder(detail.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelSuccess()
                    ApiManager.getInstance()
                            .clearCache()
                }, {
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelError(it)
                    it.printStackTrace()
                })
    }

    fun unlock() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    fun getOrderDetail() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.getRentOrderDetail(detail.freightId)
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
//                detail = t
//                callBack.onGetDetailSuccess(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.onGetDetailError(e)
//            }
//
//        }
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }
}