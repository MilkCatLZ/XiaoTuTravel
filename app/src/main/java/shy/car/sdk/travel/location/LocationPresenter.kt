package shy.car.sdk.travel.location

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.location.data.CurrentLocation

/**
 * create by LZ at 2018/05/12
 */
class LocationPresenter(context: Context, callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getCitySuccess(list: ArrayList<CurrentLocation>)
    }

    val observer = object : Observer<ArrayList<CurrentLocation>> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: ArrayList<CurrentLocation>) {
            callBack.getCitySuccess(t)
        }

        override fun onError(e: Throwable) {
//            val city = arrayOf("东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山", "东莞", "深圳", "广州", "温州", "郑州", "金华", "佛山", "上海", "苏州", "杭州", "长沙", "中山")
            val city = context.resources.getStringArray(R.array.city_array)

            val list = ArrayList<CurrentLocation>()
            city.map { list.add(CurrentLocation(it, it)) }

            callBack.getCitySuccess(list)
        }
    }

    fun getCity() {
        var o = ApiManager.instance.api.getCityList()

        ApiManager.instance.toSubscribe(o, observer)
    }
}