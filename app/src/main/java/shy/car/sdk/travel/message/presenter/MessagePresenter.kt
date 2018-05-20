package shy.car.sdk.travel.message.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.message.data.MessageList

/**
 * create by LZ at 2018/05/16
 * 活动列表
 */
class MessagePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: ArrayList<MessageList>)
        fun getListError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<MessageList> = DataBindingItemClickAdapter(R.layout.item_message, BR.message, BR.click, {})
    var pageSize = 10
    var pageIndex = 1

    init {
        val list = ArrayList<MessageList>()
        for (i in 1..9) {
            list.add(MessageList())
        }

        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getOrderList()
    }

    fun nextPage() {
        pageIndex++
        getOrderList()
    }

    private fun getOrderList() {
        ApiManager.instance.api.getMessageList(pageIndex,pageSize).subscribe(object: Observer<ArrayList<MessageList>>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: ArrayList<MessageList>) {
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.getListError(e)
            }
        })
    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }
}