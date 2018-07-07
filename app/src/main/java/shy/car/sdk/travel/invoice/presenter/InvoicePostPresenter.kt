package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.base.base.ProgressDialog
import com.base.util.DoubleUtil
import com.base.util.Log
import com.base.util.ToastManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceList

/**
 * create by LZ at 2018/07/06
 */
class InvoicePostPresenter(context: Context, var callBack: CommonCallBack<JsonObject>) : BasePresenter(context) {

    val taitouType = ObservableInt(1)
    val taitou = ObservableField<String>()
    val idCard = ObservableField<String>()
    val amount = ObservableField<String>()
    val name = ObservableField<String>()
    val phone = ObservableField<String>()
    val address = ObservableField<String>()
    val postnum = ObservableField<String>()
    lateinit var list: ArrayList<InvoiceList.Orders>


    private fun checkInput(): Boolean {

        if (taitou.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写发票抬头")
            return false
        }
        if (idCard.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写身份证号")
            return false
        }
        if (name.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写收件人")
            return false
        }
        if (phone.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写联系电话")
            return false
        }
        if (address.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写详细地址")
            return false
        }
        if (postnum.get().isNullOrEmpty()) {
            ToastManager.showShortToast(context, "请填写邮政编码")
            return false
        }

        return true
    }

    fun post() {
        if (checkInput()) {
            ProgressDialog.showLoadingView(context)
            val observable = ApiManager.getInstance()
                    .api.postTakeInvoice(taitouType.get().toString(),
                    taitou.get(),
                    idCard.get(),
                    name.get(),
                    phone.get(),
                    address.get(),
                    postnum.get(),
                    createIDString()

            )
            val observer = object : Observer<JsonObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: JsonObject) {
                    ProgressDialog.hideLoadingView(context)
                    ToastManager.showLongToast(context,"开票申请成功")
                    callBack.onSuccess(t)
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideLoadingView(context)
                    ErrorManager.managerError(context,e,"开票申请失败")
                    callBack.onError(e)
                }

            }
            ApiManager.getInstance()
                    .toSubscribe(observable, observer)
        }
    }

    private fun createIDString(): String {

        var array = JsonArray()
        list.map {
            array.add(it.id)
        }
        Log.d("sdjkfhkjshdkfjsdfkj", array.toString())
        return array.toString()
    }

    fun setLists(list: ArrayList<InvoiceList.Orders>) {
        this.list = list
        var totleAmount = 0.0
        list.map {
            totleAmount = DoubleUtil.add(totleAmount, it.money)
        }
        amount.set(totleAmount.toString())

    }
}