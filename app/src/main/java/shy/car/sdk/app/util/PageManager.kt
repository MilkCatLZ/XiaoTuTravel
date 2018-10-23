package shy.car.sdk.app.util

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.constant.ParamsConstant.*
import shy.car.sdk.app.net.ApiManager

class PageManager {

    interface BeforeNavigateListener<T> {
        fun beforeNavigate(dialog: Any, t: T)

    }

    companion object {
        fun <T> delayStartActivity(context: Context, observable: Observable<T>, path: String) {
            ProgressDialog.showLoadingView(context)
            ApiManager.getInstance()
                    .toSubscribe(observable, object : Observer<T> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: T) {
                            ProgressDialog.hideLoadingView(context)

                            ARouter.getInstance()
                                    .build(path)
                                    .withObject(Object1, t)
                                    .navigation()

                        }

                        override fun onError(e: Throwable) {
                            ProgressDialog.hideLoadingView(context)

                        }

                    })
        }

        fun <T> delayStartFragmentDialog(context: Context, observable: Observable<T>, listener: BeforeNavigateListener<T>, path: String, fragmentTransaction: FragmentTransaction, tag: String) {

            ProgressDialog.showLoadingView(context)
            ApiManager.getInstance()
                    .toSubscribe(observable, object : Observer<T> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: T) {
                            ProgressDialog.hideLoadingView(context)

                            val dialog = ARouter.getInstance()
                                    .build(path)
                                    .withObject(Object1, t).navigation() as DialogFragment
                            listener.beforeNavigate(dialog, t)
                            dialog.show(fragmentTransaction, tag)
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialog.hideLoadingView(context)

                        }

                    })
        }
    }


}