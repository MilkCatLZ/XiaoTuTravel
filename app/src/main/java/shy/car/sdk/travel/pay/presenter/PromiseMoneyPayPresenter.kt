package shy.car.sdk.travel.pay.presenter

import android.content.Context
import android.databinding.ObservableField
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.PayMethod

/**
 * 保证金
 */
class PromiseMoneyPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    lateinit var payMethod: PayMethod

    interface CallBack {
        fun onGetPromiseMoneySuccess(t: Double)
        fun getPromiseMoneyError(e: Throwable)


    }


    fun getPromiseMoney() {
        val observable = ApiManager.getInstance()
                .api.getPromiseMoney()
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: JsonObject) {
                callBack.onGetPromiseMoneySuccess(t.get("amount").toString().toDouble())
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context,e,"获取保证金失败")
                callBack.getPromiseMoneyError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }



}