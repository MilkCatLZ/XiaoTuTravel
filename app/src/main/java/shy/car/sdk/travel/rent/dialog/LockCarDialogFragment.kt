package shy.car.sdk.travel.rent.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_lock_car.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.constant.ParamsConstant.String2
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.widget.FabButton
import java.util.concurrent.TimeUnit

/**
 * create by Sharon at 2018/06/28
 * 鸣笛
 */
class LockCarDialogFragment : XTBaseDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_lock_car, null, false)
        return view
    }

    var isActionFinish = false
    var dis: Disposable? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LockCar()
        Observable.intervalRange(1, 100, 0, 100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({
                    dis = it
                })
                .subscribe({
                    if (!isActionFinish)
                        determinate.setProgress(it.toFloat())
                    else {
                        determinate.setProgress(100f)

                        dis?.dispose()
                    }
                }, {

                })
        determinate.listener = FabButton.OnAnimCompleteListener {
            txt_lock.text = "车辆已解锁"
            Observable.timer(2500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        dismissAllowingStateLoss()
                    }, {})
        }
    }

    @Autowired(name = String1)
    @JvmField
    var oid = ""
    @Autowired(name = String2)
    @JvmField
    var carID = ""

    fun LockCar() {
        val observable = ApiManager.getInstance()
                .api.carAction(carID, oid, status = 3.toString())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                isActionFinish = true
            }

            override fun onError(e: Throwable) {

                dis?.dispose()
                ErrorManager.managerError(context, e, "操作失败，请重试")
                dismissAllowingStateLoss()
//                }
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)

    }

}