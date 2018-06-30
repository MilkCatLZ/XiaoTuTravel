package shy.car.sdk.travel.rent.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_ring_car.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import java.util.concurrent.TimeUnit

/**
 * create by Sharon at 2018/06/28
 * 鸣笛
 */
class RingCarDialogFragment : XTBaseDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_ring_car, null, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content.startRippleAnimation()
        carRing()
    }

    @Autowired(name = String1)
    @JvmField
    var carid = ""

    fun carRing() {
        val observable = ApiManager.getInstance()
                .api.carAction(carid, status = 1.toString())
        val observer = object : Observer<String> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: String) {

                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            content.stopRippleAnimation()
                            activity?.let { ToastManager.showLongToast(it, "鸣笛成功") }
                            dismissAllowingStateLoss()
                        }, {

                        })

            }

            override fun onError(e: Throwable) {
//                if (BuildConfig.DEBUG) {
//
//                    Observable.timer(2, TimeUnit.SECONDS)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe({
//                                content.stopRippleAnimation()
//                                activity?.let { ToastManager.showLongToast(it, "鸣笛成功") }
//                                dismissAllowingStateLoss()
//                            }, {
//
//                            })
//                }else {
                ErrorManager.managerError(context, e, "操作失败，请重试")
                dismissAllowingStateLoss()
//                }
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}