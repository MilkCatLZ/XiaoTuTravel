package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.NearCarList

class NearCarPresenter(context:Context,var callBack:CallBack):BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<NearCarList>)
    }

//    var adapter: DataBindingItemClickAdapter<NearCarList> = DataBindingItemClickAdapter(R.layout.item_near_car_list, BR.near, BR.click, {})
    var adapter: DataBindingItemClickAdapter<NearCarList> = DataBindingItemClickAdapter(R.layout.item_near_car_list, BR.near, BR.click, {})
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
        getNearList(lat, lng)
    }

    fun nextPage() {
        pageIndex++
        getNearList(lat, lng)
    }

    private lateinit var lat: String

    private lateinit var lng: String

    fun getNearList(lat:String, lng:String) {

         this.lat=lat
         this.lng=lng

        ApiManager.instance.api.getNearList(lat,lng,pageIndex,pageSize).subscribe(object: Observer<ArrayList<NearCarList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: ArrayList<NearCarList>) {
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {

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