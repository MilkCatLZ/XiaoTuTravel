package shy.car.sdk.travel.setting.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.ImageUtil
import com.base.util.ToastManager
import com.google.gson.JsonObject
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wq.photo.widget.PickConfig
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityFeedBackBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.setting.data.FeedBack
import shy.car.sdk.travel.setting.presenter.FeedBackPresenter


/**
 * create by LZ at 2018/07/02
 *
 * 故障上报
 */
@Route(path = RouteMap.FeedBack)
class FeedBackActivity : XTBaseActivity(),
        FeedBackPresenter.CallBack {
    override fun upLoadSuccess(t: JsonObject) {
        ToastManager.showShortToast(this, "反馈成功")
        finish()
    }

    override fun getTypeSuccess(t: List<FeedBack>) {
        if (t.isNotEmpty())
            presenter.checkID.set(t[0].id)
        binding.recyclerViewBrokeType.adapter = presenter.adapter
    }

    lateinit var binding: ActivityFeedBackBinding
    lateinit var presenter: FeedBackPresenter

    @Autowired(name = Object1)
    @JvmField
    var detail: RentOrderDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_back)
        binding.ac = this

        presenter = FeedBackPresenter(this, this)
        binding.presenter = presenter

        initRecyclerView()

        presenter.getFeedBackType()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewBrokeType.layoutManager = layoutManager
    }

    fun upload() {
        presenter.upload()
    }

    fun goAlbum() {
        val per = RxPermissions(this)
        per.request(Manifest.permission.CAMERA)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(granted: Boolean) {
                        if (granted) {
                            PickConfig.with(this@FeedBackActivity)
                                    .pickMode(PickConfig.MODE_TAKE_PHOTO)
                                    .isneedcamera(true)
                                    .start()
                        } else {
                            ToastManager.showShortToast(this@FeedBackActivity, "请先允许相机权限")
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                ProgressDialog.showLoadingView(this)
                Observable.create<String> {
                    val path = ImageUtil.saveBitmapToSD(ImageUtil.compressImage(BitmapFactory.decodeFile(imgs[0]), 350), Environment.getExternalStorageDirectory().absolutePath + "/cache")
                    it.onNext(path)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            presenter.photo.set(it)
                            ProgressDialog.hideLoadingView(this)
                        }, {
                            ProgressDialog.hideLoadingView(this)
                            it.printStackTrace()
                        })


            }
        }
    }
}