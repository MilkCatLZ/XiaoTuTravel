package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingAdapter
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.DataBindingPagerAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
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
//        for (i in 1..4) {
//            carListAdapter.addItem(CarInfo())
//        }
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
        fun onCreateRentOrderSuccess(oid: String)
        fun onGetUnProgressOrderSuccess(orderMineList: OrderMineList)
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
//                if (BuildConfig.DEBUG) {
//                    var list = ArrayList<CarInfo>()
//                    var car = " {\n" +
//                            "        \"car_id\": 1001,\n" +
//                            "        \"car_model\": \"奇瑞EQ1\",\n" +
//                            "        \"car_model_img\": \"http://static.car.com/car/2018/05/10/Aosajfo12dd.jpg\",\n" +
//                            "        \"plate_number\": \"桂A88888\",\n" +
//                            "        \"color\": \"白色\",\n" +
//                            "        \"surplus_mileage\": 135,\n" +
//                            "        \"address\": \"广西南宁市西乡塘区相思湖西路\",\n" +
//                            "        \"lng\": 108.283458,\n" +
//                            "        \"lat\": 22.838222,\n" +
//                            "        \"cost\": {\n" +
//                            "            \"minute\": 0.2,\n" +
//                            "            \"kilometre\": 0.88,\n" +
//                            "            \"low_price\": \"最低消费8元\"\n" +
//                            "        },\n" +
//                            "        \"discounts\": [\n" +
//                            "            {\n" +
//                            "                \"id\": 24,\n" +
//                            "                \"txt\": \"24小时整日租\",\n" +
//                            "                \"price\": \"78\"\n" +
//                            "            },\n" +
//                            "            {\n" +
//                            "                \"id\": \"夜\",\n" +
//                            "                \"txt\": \"20:00-次日8:00\",\n" +
//                            "                \"price\": \"56\"\n" +
//                            "            }\n" +
//                            "        ]\n" +
//                            "    }"
//                    val gson = Gson()
//                    for (i in 0..5) {
//                        var carinfo = gson.fromJson(car, CarInfo::class.java)
//                        list.add(carinfo)
//                    }
//                    carListAdapter.setItems(list, 1)
//
//                    callBack.onGetCarSuccess(list)
//                }
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
        val observable = ApiManager.getInstance()
                .api.createRentCarOrder(carId, pointID)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onCreateRentOrderSuccess(t.get(ParamsConstant.OrderId).asString)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                var error = ErrorManager.managerError(context, e, "生成订单失败")

                when (error?.error_code) {
                //422101有未取消的预约订单
                    422101,422102 -> {
//                        getUnProgressOrder()
                    }
                    else -> {
                        ErrorManager.managerError(context, e, "生成订单失败")
                    }
                }

            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

//    private fun getUnPayOrder() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.getRentOrderList("1", "3", 0, 1)
//        val observer = object : Observer<List<OrderMineList>> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: List<OrderMineList>) {
//                ProgressDialog.hideLoadingView(context)
//                if (t.isNotEmpty()) {
//                    callBack.onGetUnPayOrderSuccess(t[0])
//                }
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context,e,"获取订单失败")
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }
//
//    /**
//     * 获取已预约订单
//     */
//    private fun getUnProgressOrder() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.getRentOrderList("1", "1", 0, 1)
//        val observer = object : Observer<List<OrderMineList>> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: List<OrderMineList>) {
//                ProgressDialog.hideLoadingView(context)
//                if (t.isNotEmpty()) {
//                   callBack.onGetUnProgressOrderSuccess(t[0])
//                }
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context,e,"获取订单失败")
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }


}