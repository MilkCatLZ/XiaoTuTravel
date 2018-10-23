package shy.car.sdk.travel.pay.presenter

import android.content.Context
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.user.data.User

/**
 * 保证金
 */
class PromiseMoneyPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var payMethod: PayMethod? = null
    var carSelect = ObservableField<CarSelectInfo>()

    interface CallBack {
        //        fun onGetPromiseMoneySuccess(t: Double)
//        fun getPromiseMoneyError(e: Throwable)
        fun onCreatePaySuccess(t: JsonObject)
    }


    var needPayAmount = ObservableDouble(0.0)

//    fun getPromiseMoney() {
//        ProgressDialog.showLoadingView(context)
//        val observable = ApiManager.getInstance()
//                .api.getPromiseMoney()
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onNext(t: JsonObject) {
//                ProgressDialog.hideLoadingView(context)
//                var s = t.get("needPayAmount")
//                        .asString
//
//                callBack.onGetPromiseMoneySuccess(s.toDouble())
//
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context, e, "获取保证金失败")
//                callBack.getPromiseMoneyError(e)
//            }
//
//        }
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }

    fun createPay() {
        when {
            payMethod == null -> {
                ToastManager.showShortToast(context, "请选择支付方式")
                return
            }
            carSelect.get() == null -> {
                ToastManager.showShortToast(context, "请选择车辆类型")
                return
            }
            needPayAmount.get() <= 0 -> {
                ToastManager.showShortToast(context, "无需支付保证金")
                return
            }
        }

        if (payMethod != null && carSelect.get() != null) {
            pay()
        }

    }

    private fun pay() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.createDeposits(User.instance.phone, if (User.instance.getIsDeposit()) null else carSelect.get()?.id!!, payMethod?.id.toString(), if (needPayAmount.get() > 0.0) needPayAmount.get().toString() else null)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onCreatePaySuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "创建订单失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }


}