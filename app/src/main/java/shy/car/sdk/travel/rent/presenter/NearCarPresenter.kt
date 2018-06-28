package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.eventbus.RefreshCarPointList
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.NearCarPoint

class NearCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<NearCarPoint>)
        fun onError(e: Throwable)
        fun onNearClick(nearPoint: NearCarPoint)
    }

    var adapter: DataBindingItemClickAdapter<NearCarPoint> = DataBindingItemClickAdapter(R.layout.item_near_car_list, BR.near, BR.click, {
        val nearPoint = it.tag as NearCarPoint
        callBack.onNearClick(nearPoint)
    })
    var pageSize = 50
    var pageIndex = 1


    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getNearNetWorkList()
    }

    fun nextPage() {
        pageIndex++
        getNearNetWorkList()
    }

    fun getNearNetWorkList() {

        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getNearList(app.location.cityCode, app.location.lat, app.location.lng, (pageIndex - 1) * pageSize, pageSize)
        val observer = object : Observer<ArrayList<NearCarPoint>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: ArrayList<NearCarPoint>) {
                adapter.setItems(t, 1)
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
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

    fun setItems(list: List<NearCarPoint>) {
        adapter.setItems(list, pageIndex)
    }

    private var keyWord: String = ""

    fun setKeyWord(keyword: String) {
        this.keyWord = keyword
    }

}