package shy.car.sdk.travel.pay.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack

/**
 * 保证金
 */
class PromiseReturnPresenter(context: Context, var callBack: CommonCallBack<JsonObject>) : BasePresenter(context) {


    fun promiseMoneyReturn() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.promiseMoneyRefund()
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onSuccess(t)

            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "提取保证金失败")
                callBack.onError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}