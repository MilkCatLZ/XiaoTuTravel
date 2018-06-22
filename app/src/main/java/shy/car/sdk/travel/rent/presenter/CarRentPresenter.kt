package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import android.view.View
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingAdapter
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.DataBindingPagerAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.data.CarCategory
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.NearCarPoint

/**
 * create by LZ at 2018/05/11
 */
class CarRentPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    /**
     * 上滑租车列表
     */
    var carListAdapter: DataBindingPagerAdapter<CarInfo> = DataBindingPagerAdapter(context, R.layout.item_home_car, BR.car, null)

    /**
     * 头部车辆型号
     */
    var carCategoryListAdapter: DataBindingItemClickAdapter<CarCategory> = DataBindingItemClickAdapter(R.layout.item_car_title, BR.car, BR.click, View.OnClickListener {
        val carCategory = it.tag as CarCategory
        selectedCarCaterogyID.set(carCategory.id)
    }, DataBindingAdapter.CallBack { holder, position ->
        holder.binding.setVariable(BR.presenter, this@CarRentPresenter)
    })

    var selectedCarCaterogyID = ObservableField<String>("")

    init {
        for (i in 1..7) {
            var category = CarCategory()
            category.id = i.toString()
            when (i) {
                0 -> {
                    category.carName = "全部车型"
                }
                else -> {
                    category.carName = "小兔$i"
                }

            }
            category.id = i.toString()
            carCategoryListAdapter.addItem(category)
        }
    }

    interface CallBack {
        fun onGetCarError(e: Throwable)
        fun onGetCarSuccess(t: List<CarInfo>)
        /**
         * 获取到预约未处理的订单
         */
        fun onGetUnProgressOrderSuccess(orderMineList: OrderMineList)

        /**
         * 获取到未支付的订单
         */
        fun onGetUnPayOrderSuccess(orderMineList: OrderMineList)
    }

    fun getUsableCarList(carPoint: NearCarPoint?) {
        val observable = ApiManager.getInstance()
                .api.getUsableCarList(app.location.cityCode, carPoint?.id!!, carPoint.lat, carPoint.lng)
        val observer = object : Observer<List<CarInfo>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: List<CarInfo>) {


                carListAdapter.setItems(t, 1)

                callBack.onGetCarSuccess(t)
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, "获取车辆失败")
                callBack.onGetCarError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun createRentCarOrder(carId: String, pointID: String) {

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        ApiManager.getInstance()
                .api.createRentCarOrder(carId, pointID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap {
                    ApiManager.getInstance()
                            .api.getRentOrderList("1", "1", 0, 1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                }
                .subscribe(object : Observer<List<OrderMineList>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: List<OrderMineList>) {
                        ProgressDialog.hideLoadingView(context)
                        if (t.isNotEmpty()) {
                            callBack.onGetUnProgressOrderSuccess(t[0])
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialog.hideLoadingView(context)
                        var error = ErrorManager.managerError(context, e, null)
                        createError(error)
                    }

                })
    }

    private fun createError(error: ErrorManager?) {
        ProgressDialog.hideLoadingView(context)


        when (error?.error_code) {
        //422101有未取消的预约订单
            422101 -> {
                getUnProgressOrder()
            }
            422102 -> {
                getUnPayOrder()
            }
            else -> {
                error?.showError(context, "创建订单失败")
            }
        }
    }

    private fun getUnPayOrder() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getRentOrderList("1", "3", 0, 1)
        val observer = object : Observer<List<OrderMineList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<OrderMineList>) {
                ProgressDialog.hideLoadingView(context)
                if (t.isNotEmpty()) {
                    callBack.onGetUnPayOrderSuccess(t[0])
                }
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取订单失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    /**
     * 获取已预约订单
     */
    private fun getUnProgressOrder() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getRentOrderList("1", "1", 0, 1)
        val observer = object : Observer<List<OrderMineList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<OrderMineList>) {
                ProgressDialog.hideLoadingView(context)
                if (t.isNotEmpty()) {
                    callBack.onGetUnProgressOrderSuccess(t[0])
                }
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取订单失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }


}