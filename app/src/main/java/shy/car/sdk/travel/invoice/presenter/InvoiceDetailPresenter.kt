package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceDetail

class InvoiceDetailPresenter(context: Context, var callBack: CommonCallBack<InvoiceDetail>) : BasePresenter(context) {

    var id = ""

    fun getInvoiceDetail() {
        val observable = ApiManager.getInstance()
                .api.getInvoiceDetail(id)
        val observer = object : Observer<InvoiceDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: InvoiceDetail) {
                callBack.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}