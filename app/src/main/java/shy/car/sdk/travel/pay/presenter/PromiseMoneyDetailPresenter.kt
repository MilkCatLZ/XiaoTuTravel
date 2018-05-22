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
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail

/**
 * create by LZ at 2018/05/21
 * 选择车辆
 */
class PromiseMoneyDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(list: ArrayList<PromiseMoneyDetail>)
        fun onError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<PromiseMoneyDetail> = DataBindingItemClickAdapter(R.layout.item_car_select, BR.car, BR.click, {})
    var pageSize = 50
    var pageIndex = 1

    init {
        val list = ArrayList<PromiseMoneyDetail>()
        for (i in 0..9) {
            var car = PromiseMoneyDetail()
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
        ApiManager.instance.api.getPromiseMoneyDetailList()
                .subscribe(object : Observer<ArrayList<PromiseMoneyDetail>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<PromiseMoneyDetail>) {
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