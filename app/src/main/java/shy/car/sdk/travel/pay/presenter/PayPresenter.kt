package shy.car.sdk.travel.pay.presenter

import android.content.Context
import android.databinding.ObservableDouble
import android.databinding.ObservableField
import android.view.View
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingAdapter
import com.base.databinding.DataBindingItemClickAdapter
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.PayAmount
import shy.car.sdk.travel.pay.data.PayMethod

class PayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetListSuccess(t: List<PayAmount>)
        fun onGetListError(e: Throwable)
        fun onCreatePaySuccess(t: JsonObject)

    }

    var selectedPayAmount = ObservableField<PayAmount>()
    var amount = ObservableDouble(0.0)
    var payMethod = ObservableField<PayMethod>()

    val adapter = DataBindingItemClickAdapter<PayAmount>(R.layout.item_pay_amount, BR.pay, BR.click, View.OnClickListener {

        val payAmount = it.tag as PayAmount
        selectedPayAmount.set(payAmount)
        amount.set(payAmount.price)
    }, DataBindingAdapter.CallBack { holder, position ->
        holder.binding.setVariable(BR.presenter, this@PayPresenter)
    })

    fun getPayAmountList() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getPayAmountList()
        val observer = object : Observer<List<PayAmount>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<PayAmount>) {
                ProgressDialog.hideLoadingView(context)
                if (t.isNotEmpty()) {
                    adapter.setItems(t, 1)
                    selectedPayAmount.set(t[0])
                }
                callBack.onGetListSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                if (BuildConfig.DEBUG) {
                    for (i in 0..5) {
                        val payAmount = PayAmount()
                        payAmount.price = i + 10.0
                        payAmount.realPrice = i + 9.9
                        adapter.items.add(payAmount)
                    }
                    selectedPayAmount.set(adapter.items[0])
                }
                callBack.onGetListError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun pay() {
        if (payMethod.get() == null) {
            ToastManager.showShortToast(context, "请选择支付方式")
            return
        }
        if (selectedPayAmount.get() == null && amount.get() == 0.0) {
            ToastManager.showShortToast(context, "请选择充值金额")
            return
        }
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        var price: String = if (amount.get() > 0.0) {
            amount.get()
                    .toString()
        } else {
            selectedPayAmount.get()
                    ?.realPrice.toString()
        }

        val observable = ApiManager.getInstance()
                .api.createRecharge(price, payMethod.get()?.id.toString())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onCreatePaySuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "支付失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}