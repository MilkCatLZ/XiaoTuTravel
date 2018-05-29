package shy.car.sdk.travel.common.presenter

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.send.adapter.WheelAdapter
import shy.car.sdk.travel.common.data.CommonWheelItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * 选择用车时间
 */
class SendTimeSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetListSuccess(list: ArrayList<CommonWheelItem>)
    }

    var dateList = ArrayList<CommonWheelItem>()
    var timeList = ArrayList<CommonWheelItem>()
    var adapterDate = WheelAdapter()
    var adapterTime = WheelAdapter()

    fun getList() {

        var calendar = Calendar.getInstance()
        var format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        Observable.just(0, 1, 2, 3, 4).subscribe(object : Observer<Int> {
            override fun onComplete() {
                adapterDate.list.addAll(dateList)
                callBack.onGetListSuccess(dateList)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Int) {
                calendar.add(Calendar.DAY_OF_MONTH, t)
                val name = format.format(calendar.time)
                val item = CommonWheelItem()
                item.name = name
                dateList.add(item)
            }

            override fun onError(e: Throwable) {

            }
        })

        var item = CommonWheelItem()
        item.name = "全天"
        timeList.add(item)
        var item1 = CommonWheelItem()
        item1.name = "晚上(18:00-24:00)"
        timeList.add(item1)

        adapterTime.list.addAll(timeList)
    }
}