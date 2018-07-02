package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.StringUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.order.net.OrderManager
import shy.car.sdk.travel.rent.data.NearCarPoint

/**
 * create by LZ at 2018/05/11
 */
class CarRentOrderingPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var selectedCarCaterogyID = ObservableField<String>("")

    interface CallBack {
        fun onGetCarError(e: Throwable)
        /**
         * 获取到预约未处理的订单
         */
        fun onGetUnProgressOrderSuccess(orderMineList: OrderMineList?)

        fun getDetailSuccess(t: RentOrderDetail)
        fun getNetWorkListSuccess(t: ArrayList<NearCarPoint>)
    }

    fun getRentOrderDetail(id: String) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        OrderManager.getOrderDetail(id, false, callBack = object : OrderManager.GetDetailCallBack {
            override fun getDetailSuccess(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                callBack.getDetailSuccess(t)
            }

            override fun getDetailError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取订单失败")
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

        })
    }

    fun getNetWorkList() {

        Observable.create<String> {
            while (StringUtils.isEmpty(app.location.cityCode)) {
                try {
                    Thread.sleep(100)
                } catch (_: Exception) {

                }
            }
            it.onNext(app.location.cityCode)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val observable = ApiManager.getInstance()
                            .api.getNearList(it, car = selectedCarCaterogyID.get())
                    val observer = object : Observer<ArrayList<NearCarPoint>> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: ArrayList<NearCarPoint>) {
                            callBack.getNetWorkListSuccess(t)
                        }

                        override fun onError(e: Throwable) {

                        }
                    }

                    ApiManager.getInstance()
                            .toSubscribe(observable, observer)
                }, {

                })


    }


}