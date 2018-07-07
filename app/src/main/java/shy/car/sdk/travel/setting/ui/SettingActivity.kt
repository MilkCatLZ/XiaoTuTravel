package shy.car.sdk.travel.setting.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.FileManager
import com.base.util.ToastManager
import com.base.util.Version
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setting.*
import okhttp3.ResponseBody
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivitySettingBinding
import shy.car.sdk.travel.update.UpdateHelper
import shy.car.sdk.travel.user.data.User
import java.io.File
import java.text.DecimalFormat

/**
 * create by LZ at 2018/05/16
 */
@Route(path = RouteMap.Setting)
class SettingActivity : XTBaseActivity() {
    val version = ObservableField<String>("")
    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.ac = this
        binding.user = User.instance
        version.set(Version.getVersion(this))
        showCache()
    }


    fun clearCache() {

        Observable.create<String> {
            FileManager.clearCache()
            FileManager.clearCache(Glide.getPhotoCacheDir(this@SettingActivity))
            Thread.sleep(2000)
            it.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {

                        ToastManager.showShortToast(this@SettingActivity, R.string.str_clean_success)
                        showCache()
                    }

                    override fun onSubscribe(d: Disposable) {
                        ToastManager.showShortToast(this@SettingActivity, R.string.str_cleanning)
                    }

                    override fun onNext(t: String) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }


    private fun showCache() {

        Observable.create<Float> {
            val size = FileManager.getFolderSize(File(FileManager.getDirectory(this@SettingActivity)))
            val sizes = FileManager.getFolderSize(Glide.getPhotoCacheDir(this@SettingActivity))

            it.onNext(size + sizes)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    if (it == 0f) {
                        txtSettingCache.text = "0m"
                    } else {
                        val format = DecimalFormat("#.#")
                        txtSettingCache.text = (format.format(it) + " M")
                    }
                }, {})
    }

    var disposable: Disposable? = null
    fun checkUpdate() {
        disposable?.dispose()
        ProgressDialog.showLoadingView(this)
        val observable = ApiManager.getInstance()
                .api.getUpdateInfo()
        val observer = object : Observer<ResponseBody> {
            override fun onComplete() {
                ProgressDialog.hideLoadingView(this@SettingActivity)
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: ResponseBody) {
                UpdateHelper(this@SettingActivity, R.mipmap.ic_launcher, false, true).checkUpdate(String(t.bytes()))
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(this@SettingActivity, e, "更新失败，请稍后再试")
                ProgressDialog.hideLoadingView(this@SettingActivity)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)


    }

    fun logout() {
        app.logout()
        finish()
    }

    fun about() {
        ARouter.getInstance()
                .build(RouteMap.About)
                .navigation()
    }

    fun changeMobile() {
        ARouter.getInstance()
                .build(RouteMap.ChangeMobile)
                .navigation()
    }
    fun feedBack() {
        ARouter.getInstance()
                .build(RouteMap.FeedBack)
                .navigation()
    }
}