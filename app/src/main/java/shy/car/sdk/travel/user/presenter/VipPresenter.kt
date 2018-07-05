package shy.car.sdk.travel.user.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.BR
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.Rank
import shy.car.sdk.travel.user.data.User

class VipPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onGetRankListSuccess(list: List<Rank>)
    }

    var list = ArrayList<Rank>()
    val adapter = DataBindingItemClickAdapter<Rank>(R.layout.item_rank, BR.rank, BR.click, {}) { holder, position ->
        holder.binding.setVariable(BR.user, User.instance)
        if (position == 0) {
            holder.binding.setVariable(BR.isFirst, true)
            holder.binding.setVariable(BR.isLast, false)
        } else if (position == list.size - 1) {
            holder.binding.setVariable(BR.isLast, true)
            holder.binding.setVariable(BR.isFirst, false)
        } else {
            holder.binding.setVariable(BR.isLast, false)
            holder.binding.setVariable(BR.isFirst, false)
        }
        if (position < list.size - 1) {
            holder.binding.setVariable(BR.nextScore, list[position + 1].score)
        } else {
            holder.binding.setVariable(BR.nextScore, list[position].score + 50000)
        }
    }


    fun getRankList() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.getRankList()
        val observer = object : Observer<List<Rank>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<Rank>) {
                ProgressDialog.hideLoadingView(context)
                list.clear()
                list.addAll(t)
                adapter.setItems(t, 1)
                callBack.onGetRankListSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}