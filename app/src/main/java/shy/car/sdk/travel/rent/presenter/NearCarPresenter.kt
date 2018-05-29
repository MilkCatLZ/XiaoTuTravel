package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.rent.data.NearCarList

class NearCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<NearCarList>)
        fun onError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<NearCarList> = DataBindingItemClickAdapter(R.layout.item_near_car_list, BR.near, BR.click, {
        ARouter.getInstance()
                .build(RouteMap.CarPointDetail)
                .navigation()
    })
    var pageSize = 50
    var pageIndex = 1

    init {
        val list = ArrayList<NearCarList>()
        for (i in 1..9) {
            list.add(NearCarList())
        }

        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getNearList()
    }

    fun nextPage() {
        pageIndex++
        getNearList()
    }

    fun getNearList() {

        disposable?.dispose()
        ApiManager.getInstance().api.getNearList(app.location.lat.toString(), app.location.lng.toString(), keyWord, pageIndex, pageSize)
                .subscribe(object : Observer<ArrayList<NearCarList>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<NearCarList>) {
                        EventBus.getDefault()
                                .post(t)
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

    fun setItems(list: List<NearCarList>) {
        adapter.setItems(list, pageIndex)
    }

    private var keyWord: String = ""

    fun setKeyWord(keyword: String) {
        this.keyWord = keyword
    }

}