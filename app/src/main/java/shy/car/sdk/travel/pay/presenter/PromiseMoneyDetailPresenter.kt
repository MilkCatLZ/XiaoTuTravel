package shy.car.sdk.travel.pay.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail

/**
 * create by LZ at 2018/05/21
 * 选择车辆
 */
class PromiseMoneyDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: List<PromiseMoneyDetail>)
        fun onError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<PromiseMoneyDetail> = DataBindingItemClickAdapter(R.layout.item_promise_money_detail, BR.detail, BR.click, {})
    var pageSize = 10
    var pageIndex = 1

    init {
//        val list = ArrayList<PromiseMoneyDetail>()
//        for (i in 0..9) {
//            var car = PromiseMoneyDetail()
//            list.add(car)
//        }
//
//        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getDepositsLogs()
    }

    fun nextPage() {
        pageIndex++
        getDepositsLogs()
    }

    fun getDepositsLogs() {

        disposable?.dispose()
        ApiManager.getInstance()
                .api.getDepositsLogs((pageIndex - 1) * pageSize, pageSize)
                .subscribe(object : Observer<List<PromiseMoneyDetail>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: List<PromiseMoneyDetail>) {
                        adapter.setItems(t, pageIndex)
                        callBack.getListSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        callBack.onError(e)
                    }
                })
    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }

    fun setItems(list: List<PromiseMoneyDetail>) {
        adapter.setItems(list, pageIndex)
    }

}