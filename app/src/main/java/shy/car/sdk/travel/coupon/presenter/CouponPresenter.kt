package shy.car.sdk.travel.coupon.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.coupon.data.Coupon

/**
 * create by lz at 2018/06/18
 * 银行卡管理
 */
class CouponPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onGetListSuccess(t: List<Coupon>)
        fun onGetListError(e: Throwable)
    }

    var pageSize = 10
    var pageIndex = 1

    var adapter = DataBindingItemClickAdapter<Coupon>(R.layout.item_coupon, BR.coupon, BR.click, {
    })

    fun getCouponList() {
        val observable = ApiManager.getInstance()
                .api.getCouponList((pageIndex - 1) * pageSize, pageSize)

        val observer = object : Observer<List<Coupon>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<Coupon>) {
                adapter.setItems(t, 1)
                callBack.onGetListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onGetListError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun refresh() {
        pageIndex = 1
        getCouponList()
    }

    fun getTotal(): Int {
        return 1
    }

    fun nextPage() {
        pageIndex++
        getCouponList()
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

}