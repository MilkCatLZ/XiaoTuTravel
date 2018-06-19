package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingItemClickAdapter
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.rent.data.NearCarPoint

class NearCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<NearCarPoint>)
        fun onError(e: Throwable)
        fun onNearClick(nearPoint: NearCarPoint)
    }

    var adapter: DataBindingItemClickAdapter<NearCarPoint> = DataBindingItemClickAdapter(R.layout.item_near_car_list, BR.near, BR.click, {

        val nearPoint=it.tag as NearCarPoint
        callBack.onNearClick(nearPoint)

//        ARouter.getInstance()
//                .build(RouteMap.CarPointDetail)
//                .navigation()
    })
    var pageSize = 50
    var pageIndex = 1

    init {
//        val list = ArrayList<NearCarPoint>()
//        for (i in 1..9) {
//            list.add(NearCarPoint())
//        }
//
//        adapter.setItems(list, 1)
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
        val observable = ApiManager.getInstance()
                .api.getNearList(app.location.cityCode, if (app.location.lat != 0.0) 22.817746.toString() else app.location.lat.toString(), if (app.location.lng != 0.0) 108.36637.toString() else app.location.lng.toString(), (pageIndex - 1) * pageSize, pageSize)
        val observer = object : Observer<ArrayList<NearCarPoint>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: ArrayList<NearCarPoint>) {
                EventBus.getDefault()
                        .post(t)
                adapter.setItems(t, 1)
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                var list = ArrayList<NearCarPoint>()
                if (BuildConfig.DEBUG) {
                    var gson = Gson()
                    var s = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"南宁万达时代广场\",\n" +
                            "    \"address\": \"西乡塘鲁班路10号\",\n" +
                            "    \"lng\": 108.248593,\n" +
                            "    \"lat\": 22.921449,\n" +
                            "    \"usable_cars_num\": 10,\n" +
                            "    \"usable_parking_place\": 10\n" +
                            "}"
                    for (i in 0..5) {
                        val nearCarPoint = gson.fromJson(s, NearCarPoint::class.java)
                        list.add(nearCarPoint)
                    }
                }
                EventBus.getDefault()
                        .post(list)
                callBack.onError(e)
                callBack.getListSuccess(list)
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