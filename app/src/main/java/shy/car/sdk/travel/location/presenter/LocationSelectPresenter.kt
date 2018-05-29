package shy.car.sdk.travel.location.presenter

import android.content.Context
import com.amap.api.services.core.PoiItem
import com.base.databinding.DataBindingItemClickAdapter
import com.base.location.AmapLocationManager
import com.base.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.presenter.BasePresenter
import java.util.concurrent.TimeUnit

/**
 * create by LZ at 2018/05/29
 * 选择位置
 */
class LocationSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onAddressClick(poiItem: PoiItem)
        fun getPoiListSuccess()

    }

    var searchDispose: Disposable? = null

    var adapter: DataBindingItemClickAdapter<PoiItem> = DataBindingItemClickAdapter(R.layout.item_location_select, BR.item, BR.click, {
        callBack.onAddressClick(it.tag as PoiItem)
    })
    init{
        adapter.disableFooter()
    }

    fun getAddressList(keyWord: String) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    AmapLocationManager.getInstance()
                            .searchPoiList(keyWord, app.location.cityCode, 1)
                }
                .subscribe(object : Observer<ArrayList<PoiItem>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        searchDispose?.dispose()
                        searchDispose = d
                    }

                    override fun onNext(list: ArrayList<PoiItem>) {
                        adapter.setItems(list, 1)
callBack.getPoiListSuccess()
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }
}