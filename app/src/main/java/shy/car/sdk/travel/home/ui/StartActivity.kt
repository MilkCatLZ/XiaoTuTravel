package shy.car.sdk.travel.home.ui

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Device
import com.base.util.Version
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*
import shy.car.sdk.MainActivity
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityStartBinding
import shy.car.sdk.travel.home.data.StartInfo
import shy.car.sdk.travel.home.presenter.StartPresenter
import java.io.ByteArrayOutputStream
import java.sql.Time
import java.util.concurrent.TimeUnit

class StartActivity : XTBaseActivity(),
        StartPresenter.AdListener {
    override fun getAdError() {
        normalMode()
    }

    lateinit var presenter: StartPresenter
    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        binding.ac = this
        initPresenter()

        presenter.getStartInfo()
    }

    override fun getAdSuccess(startInfo: StartInfo) {
        try {
            if (Version.isFirstInstall(this)) {
                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            ARouter.getInstance()
                                    .build(RouteMap.Guild)
                                    .navigation()
                        }, {})

            } else if (startInfo.needAD() && !isDestroyed) {
                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            adMode(startInfo)
                        }, {})

            } else {
                normalMode()
            }
        } catch (e: Exception) {

        }
    }

    private fun normalMode() {
        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    skip()
                }, {})
    }

    var dispose: Disposable? = null
    private fun adMode(startInfo: StartInfo) {
        Glide.with(this)
                .load(startInfo.adUrl)
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        val baos = ByteArrayOutputStream()
                        resource?.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                        val bytes = baos.toByteArray()
                        Glide.with(this@StartActivity)
                                .load(bytes)
                                .crossFade()
                                .into(imageStart)

                        txtAdCountDown.visibility = View.VISIBLE
                        Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                                .flatMap {
                                    Observable.just(5 - it)
                                }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer<Long> {
                                    override fun onComplete() {
                                        skip()
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                        dispose = d
                                    }

                                    override fun onNext(t: Long) {
                                        txtAdCountDown.text = "跳过$t"
                                    }

                                    override fun onError(e: Throwable) {

                                    }

                                })
                    }
                })
    }


    private fun initPresenter() {
        presenter = StartPresenter(this, this)
    }

    fun skip() {
        if (!isDestroyed) {
            startActivity(MainActivity::class.java)
            finish()
        }
    }

    override fun onDestroy() {
        dispose?.dispose()
        super.onDestroy()
    }
}