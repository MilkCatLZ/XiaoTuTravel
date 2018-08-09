package shy.car.sdk.travel.bank.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.BR
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.bank.data.BankType

class BankTypeSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetListSuccess(t: List<BankType>)
        fun onGetListError(e: Throwable)
        fun onItemClick(bankType: BankType)

    }

    val adapter = DataBindingItemClickAdapter<BankType>(R.layout.item_bank_type, BR.type, BR.click) {
        val bankType=it.tag as BankType

        callBack.onItemClick(bankType)
    }

    fun getBankTypeList() {
        val observable = ApiManager.getInstance()
                .api.getBankTypedList()
        val observer = object : Observer<List<BankType>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<BankType>) {
                adapter.setItems(t, 1)
                callBack.onGetListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onGetListError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}