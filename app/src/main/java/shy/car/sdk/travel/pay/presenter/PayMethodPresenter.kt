package shy.car.sdk.travel.pay.presenter

import android.content.Context
import android.databinding.ObservableInt
import android.view.View
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.PayMethod

class PayMethodPresenter(context: Context, var listener: GetPayMethodListener? = null) : BasePresenter(context) {

    interface GetPayMethodListener {
        fun onGetListSuccess(list: Any)
        fun onPayMethodClick(tag: PayMethod)

    }

    var type = 0
    var checkPayID = ObservableInt(0)

    var adapter = DataBindingItemClickAdapter<PayMethod>(R.layout.item_pay_method, BR.pay, BR.click, View.OnClickListener {
        var tag = it.tag
        when (tag) {
            is PayMethod -> {
                checkPayID.set(tag.id)
                listener?.onPayMethodClick(tag)
            }
        }

    }, com.base.databinding.DataBindingAdapter.CallBack { holder, _ ->
        holder.binding.setVariable(BR.presenter, this@PayMethodPresenter)
    })

    fun getPayMethodList() {
        var observable = ApiManager.getInstance()
                .api.getPayMethod(type)
        var observer = object : Observer<List<PayMethod>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(list: List<PayMethod>) {
                if (checkPayID.get() == 0)
                    checkPayID.set(list[0].id)
                adapter.setItems(list, 1)
                listener?.onGetListSuccess(list)
            }

            override fun onError(e: Throwable) {

            }
        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}