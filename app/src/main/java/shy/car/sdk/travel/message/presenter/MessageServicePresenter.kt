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
 * 系统消息列表
 */
class MessageServicePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: ArrayList<MessageList>)
        fun getListError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<MessageList> = DataBindingItemClickAdapter(R.layout.item_message_service, BR.message, BR.click, {})
    var pageSize = 10
    var pageIndex = 1

    init {
//        val list = ArrayList<MessageList>()
//        for (i in 1..9) {
//            list.add(MessageList())
//        }

//        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getMessageList()
    }

    fun nextPage() {
        pageIndex++
        getMessageList()
    }

    private fun getMessageList() {


        val observable = ApiManager.getInstance()
                .api.getMessageList(2, (pageIndex - 1) * pageSize, pageSize)//固定2:服务消息
        val observer = object : Observer<ArrayList<MessageList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: ArrayList<MessageList>) {
                adapter.setItems(t, pageIndex)
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.getListError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }
}