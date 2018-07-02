package shy.car.sdk.travel.order.net

import com.base.base.ProgressDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.travel.order.data.RentOrderDetail

class OrderManager {
    interface GetDetailCallBack {
        fun getDetailSuccess(t: RentOrderDetail)
        fun getDetailError(e: Throwable)
        fun onSubscribe(d: Disposable)
    }

    companion object {
        fun getOrderDetail(oid: String, needPost: Boolean = false, callBack: GetDetailCallBack?) {


            val observable = ApiManager.getInstance()
                    .api.getRentOrderDetail(oid)
            val observer = object : Observer<RentOrderDetail> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                    callBack?.onSubscribe(d)
                }

                override fun onNext(t: RentOrderDetail) {
                    callBack?.getDetailSuccess(t)

                }

                override fun onError(e: Throwable) {
                    callBack?.getDetailError(e)
                }

            }
            ApiManager.getInstance()
                    .toSubscribe(observable, observer)
        }
    }
}