package shy.car.sdk.travel.pay.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.CarSelectInfo

/**
 * create by LZ at 2018/05/21
 * 选择车辆
 */
class CarSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<CarSelectInfo>)
        fun onError(e: Throwable)
        fun onCarSelected(carSelectInfo: CarSelectInfo)
    }

    var adapter: DataBindingItemClickAdapter<CarSelectInfo> = DataBindingItemClickAdapter(R.layout.item_car_select, BR.car, BR.click, {
        callBack.onCarSelected(it.tag as CarSelectInfo)
    })
    var pageSize = 50
    var pageIndex = 1

    init {
        val list = ArrayList<CarSelectInfo>()
        for (i in 0..9) {
            var car = CarSelectInfo()
            when (i % 3) {
                0 -> {
                    car.carName = "小兔e1"
                    car.id = i.toString()
                    car.promiseMoneyPrice = 999.0
                }
                1 -> {
                    car.carName = "小兔e2"
                    car.id = i.toString()
                    car.promiseMoneyPrice = 1999.0
                }
                2 -> {
                    car.carName = "小兔e3"
                    car.id = i.toString()
                    car.promiseMoneyPrice = 2999.0
                }
            }

            list.add(car)
        }

        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getCarList()
    }

    fun nextPage() {
        pageIndex++
        getCarList()
    }

    private lateinit var lat: String

    private lateinit var lng: String
    var disposable: Disposable? = null
    fun getCarList() {

        this.lat = lat
        this.lng = lng
        disposable?.dispose()
        ApiManager.instance.api.getCarList()
                .subscribe(object : Observer<ArrayList<CarSelectInfo>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<CarSelectInfo>) {
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

    fun setItems(list: List<CarSelectInfo>) {
        adapter.setItems(list, pageIndex)
    }

}