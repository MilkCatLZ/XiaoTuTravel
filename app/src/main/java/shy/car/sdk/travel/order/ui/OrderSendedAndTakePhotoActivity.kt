package shy.car.sdk.travel.order.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Bundle
import android.os.Environment
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
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.eventbus.SendSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityOrderSendedTakePhotoBinding
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import shy.car.sdk.travel.order.presenter.OrderSendedPresenter

/**
 * 拍照已送达
 */
@Route(path = RouteMap.OrderSendedPhoto)
class OrderSendedAndTakePhotoActivity : XTBaseActivity(),
        OrderSendedPresenter.CallBack {

    @Autowired(name = Object1)
    @JvmField
    var detail: DeliveryOrderDetail? = null
    private var photoID = 0
    override fun upLoadSuccess(t: JsonObject) {
        ToastManager.showShortToast(this, "提交成功")
        eventBusDefault.post(SendSuccess())
        eventBusDefault.post(RefreshOrderList())
        finish()
    }

    lateinit var binding: ActivityOrderSendedTakePhotoBinding
    lateinit var presenter: OrderSendedPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_sended_take_photo)
        presenter = OrderSendedPresenter(this, this)
        presenter.detail = detail
        binding.ac = this
        binding.presenter = presenter
    }

    fun upload() {
        presenter.upload()
    }

    fun goAlbum(photoID: Int) {
        this.photoID = photoID
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
                            PickConfig.with(this@OrderSendedAndTakePhotoActivity)
                                    .pickMode(PickConfig.MODE_TAKE_PHOTO)
                                    .isneedcamera(true)
                                    .start()
                        } else {
                            ToastManager.showShortToast(this@OrderSendedAndTakePhotoActivity, "请先允许相机权限")
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
                    val path = ImageUtil.saveBitmapToSD(BitmapFactory.decodeFile(imgs[0]), Environment.getExternalStorageDirectory().absolutePath + "/cache", 35)
                    it.onNext(path)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            when (photoID) {
                                1 -> {
                                    presenter.photo1.set(it)
                                    presenter.uploadPhoto1()
                                }
                                2 -> {
                                    presenter.photo2.set(it)
                                    presenter.uploadPhoto2()
                                }
                            }

                            ProgressDialog.hideLoadingView(this)
                        }, {
                            ProgressDialog.hideLoadingView(this)
                            it.printStackTrace()
                        })


            }
        }
    }

    fun retryPhoto1Upload() {
        presenter.uploadPhoto1()
    }

    fun retryPhoto2Upload() {
        presenter.uploadPhoto2()
    }
}