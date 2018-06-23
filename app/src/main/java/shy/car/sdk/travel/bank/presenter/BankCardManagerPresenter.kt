package shy.car.sdk.travel.bank.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.user.data.User

/**
 * create by lz at 2018/06/18
 * 银行卡管理
 */
class BankCardManagerPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onGetListSuccess(t: List<BankCard>)
        fun onGetListError(e: Throwable)
        fun onBankSelected(bank: BankCard)

    }

    var selectedMode = false

    var adapter = DataBindingItemClickAdapter<BankCard>(R.layout.item_bank_card, BR.bank, BR.click, {
        if (selectedMode) {
            val bank = it.tag as BankCard
            EventBus.getDefault()
                    .post(bank)
            callBack.onBankSelected(bank)
        }
    })

    fun getBankCardList() {
        val observable = ApiManager.getInstance()
                .api.getBankCardList(User.instance.phone)

        val observer = object : Observer<List<BankCard>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<BankCard>) {
                adapter.setItems(t, 1)
                callBack.onGetListSuccess(t)
            }

            override fun onError(e: Throwable) {
                if (BuildConfig.DEBUG) {
                    for (i in 0..5) {
                        var bank = BankCard()
                        bank.name = "中国银行$i"
                        bank.card = "6222 02** **** ****53$i"
                        bank.id = i.toString()
                        adapter.addItem(bank)
                    }
                }
                callBack.onGetListError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun refresh() {
        getBankCardList()
    }

}