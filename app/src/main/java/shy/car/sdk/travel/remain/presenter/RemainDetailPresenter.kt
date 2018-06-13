package shy.car.sdk.travel.remain.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.remain.data.RemainList

/**
 * create by LZ at 2018/06/13
 * 我的余额
 */
class RemainDetailPresenter(context: Context, var callback: OnRemainCallBack? = null) : BasePresenter(context) {
    interface OnRemainCallBack {
        fun onGetListSuccess(list: List<RemainList>)
        fun onGetListError()

    }

    var pageIndex = 1
    var pageSize = 10
    var adapter = DataBindingItemClickAdapter<RemainList>(R.layout.item_remain_detail, BR.remain, BR.click, {})

    fun getRemainDetailList() {
        var observable = ApiManager.getInstance()
                .api.getRemainDetailList((pageIndex - 1) * pageSize, pageSize)
        var observer = object : Observer<List<RemainList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(list: List<RemainList>) {
                callback?.onGetListSuccess(list)
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, null)
                callback?.onGetListError()
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getRemainDetailList()
    }

    fun getTotal(): Int {
        return if (hasMore()) adapter.adapterItemCount + 5 else adapter.adapterItemCount
    }

    fun nextPage() {
        pageIndex++
        getRemainDetailList()
    }

}