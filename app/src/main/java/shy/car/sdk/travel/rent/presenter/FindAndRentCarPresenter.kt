package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.RentOrderDetail

/**
 * 找车取车
 */
class FindAndRentCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    var carInfo = CarInfo()
    var orderID: String = ""


    interface CallBack {
        fun onRingError(e: Throwable)
        fun onCancelSuccess()
        fun onCancelError(e: Throwable)
        fun onGetDetailError(e: Throwable)
        fun onGetDetailSuccess(t: RentOrderDetail)

    }

    fun carRing() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.carAction(carInfo.carId, status = 1.toString())
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
        val observable = ApiManager.getInstance()
                .api.carAction(carInfo.carId, orderID, status = 3.toString())
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
                ErrorManager.managerError(context, e, "操作失败，请重试")
                ProgressDialog.hideLoadingView(context)
                callBack.onRingError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun cancelOrder() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.cancelRentOrder(orderID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelSuccess()
                }, {
                    ProgressDialog.hideLoadingView(context)
                    callBack.onCancelError(it)
                    it.printStackTrace()
                })
    }

    fun getOrderDetail() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getRentOrderDetail(orderID)
        val observer = object : Observer<RentOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}