package shy.car.sdk.travel.setting.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.FileManager
import com.base.util.ToastManager
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setting.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivitySettingBinding
import java.io.File
import java.text.DecimalFormat

/**
 * create by LZ at 2018/05/16
 */
@Route(path = RouteMap.Setting)
class SettingActivity : XTBaseActivity() {

    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.ac = this
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
            size + sizes
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    if (it == 0f) {
                        txtSettingCache.text = "0m"
                    } else {
                        val format = DecimalFormat("#.#")
                        txtSettingCache.text = (format.format(it) + " M")
                    }
                }
    }
}