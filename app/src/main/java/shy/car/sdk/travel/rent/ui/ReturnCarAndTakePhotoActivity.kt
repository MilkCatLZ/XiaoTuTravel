package shy.car.sdk.travel.rent.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.ImageUtil
import com.base.util.ToastManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wq.photo.widget.PickConfig
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.constant.ParamsConstant.String2
import shy.car.sdk.app.data.ReturnCarSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityReturnCarAndTakePhotoBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.presenter.ReturnCarAndTakePhotoPresenter

/**
 * create by 过期猫粮 at 2018/06/19
 * 拍照并取车计费
 */
@Route(path = RouteMap.ReturnCarUploadPhoto)
class ReturnCarAndTakePhotoActivity : XTBaseActivity(),
        ReturnCarAndTakePhotoPresenter.CallBack {
    override fun onReturnSuccess() {
        ToastManager.showShortToast(this, "还车成功")
        EventBus.getDefault()
                .post(ReturnCarSuccess(presenter.detail?.orderId!!))
        finish()
    }

    override fun onReturnError() {

    }

    override fun onGetDetailSuccess(t: RentOrderDetail) {

    }

    override fun onGetDetailError(e: Throwable) {

    }

    @Autowired(name = String1)
    @JvmField
    var oid = ""
    @Autowired(name = String2)
    @JvmField
    var netWorkID = ""

    lateinit var presenter: ReturnCarAndTakePhotoPresenter
    var isLeft = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance()
                .inject(this)

        var binding = DataBindingUtil.setContentView<ActivityReturnCarAndTakePhotoBinding>(this, R.layout.activity_return_car_and_take_photo)
        binding.ac = this

        presenter = ReturnCarAndTakePhotoPresenter(this, this)
        binding.presenter = presenter

        presenter.netWorkID=netWorkID
        presenter.getOrderDetail(orderID = oid)
    }

    fun submitAndUnLock() {
        presenter.uploadPicAndUnlockCar()
    }

    fun leftPhotoClick() {
        checkPermission()
        isLeft = true
    }

    fun rightPhotoClick() {
        checkPermission()
        isLeft = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                ProgressDialog.showLoadingView(this)
                Observable.create<String> {
                    val path = ImageUtil.saveBitmapToSD(ImageUtil.compressImage(BitmapFactory.decodeFile(imgs[0])), Environment.getExternalStorageDirectory().absolutePath + "/cache")
                    it.onNext(path)
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            ProgressDialog.hideLoadingView(this)
                            when {
                                isLeft -> {
                                    presenter.leftImage.set(it)
                                }
                                else -> {
                                    presenter.rightImage.set(it)
                                }
                            }
                        }, {
                            ProgressDialog.hideLoadingView(this)
                            it.printStackTrace()
                        })


            }
        }
    }

    private fun checkPermission() {

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
                            PickConfig.with(this@ReturnCarAndTakePhotoActivity)
                                    .pickMode(PickConfig.MODE_SINGLE_PICK)
                                    .isneedcamera(true)
                                    .start()
                        } else {
                            ToastManager.showShortToast(this@ReturnCarAndTakePhotoActivity, "请先允许相机权限")
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })

    }
}