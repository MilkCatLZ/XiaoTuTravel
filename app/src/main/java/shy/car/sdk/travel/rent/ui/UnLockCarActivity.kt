package shy.car.sdk.travel.rent.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
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
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.eventbus.UnLockSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityUnlockCarBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.dialog.UnLockCarDialogFragment
import shy.car.sdk.travel.rent.presenter.UnLockCarPresenter

/**
 * create by 过期猫粮 at 2018/06/19
 * 拍照并取车计费
 * 租车拍照
 */
@Route(path = RouteMap.UnLockCar)
class UnLockCarActivity : XTBaseActivity(),
        UnLockCarPresenter.CallBack {
    override fun onUnLockSuccess() {

        unlockAnim.dialog.setOnDismissListener {
            ToastManager.showShortToast(this, "车辆已解锁，请及时上车")
            ARouter.getInstance()
                    .build(RouteMap.Driving)
                    .withString(String1, oid)
                    .navigation()
            RefreshOrderList.refreshOrderList()
            EventBus.getDefault()
                    .post(UnLockSuccess())
            finish()
        }
        unlockAnim.finish()
    }

    override fun onUnLockError() {
        unlockAnim.dismissAllowingStateLoss()
    }

    override fun onGetDetailSuccess(t: RentOrderDetail) {

    }

    override fun onGetDetailError(e: Throwable) {

    }

    @Autowired(name = String1)
    @JvmField
    var oid = ""

    lateinit var presenter: UnLockCarPresenter
    var isLeft = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance()
                .inject(this)

        var binding = DataBindingUtil.setContentView<ActivityUnlockCarBinding>(this, R.layout.activity_unlock_car)
        binding.ac = this

        presenter = UnLockCarPresenter(this, this)
        binding.presenter = presenter

        presenter.getOrderDetail(orderID = oid)
    }

    var unlockAnim = UnLockCarDialogFragment()
    fun submitAndUnLock() {
        if (presenter.uploadPicAndUnlockCar()) {
            unlockAnim.show(supportFragmentManager, "fragment_unlock_dialog")
        }
    }

    fun leftPhotoClick() {
        checkPermission()
        isLeft = true
    }

    fun rightPhotoClick() {
        checkPermission()
        isLeft = false
    }

    fun retryRightUpload() {
        presenter.uploadRightCarImage()
    }

    fun retryLeftUpload() {
        presenter.uploadLeftCarImage()
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
                            when {
                                isLeft -> {
                                    presenter.leftImage.set(it)
                                    presenter.uploadLeftCarImage()
                                }
                                else -> {
                                    presenter.rightImage.set(it)
                                    presenter.uploadRightCarImage()
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
                            PickConfig.with(this@UnLockCarActivity)
                                    .pickMode(PickConfig.MODE_TAKE_PHOTO)
                                    .isneedcamera(true)
                                    .start()
                        } else {
                            ToastManager.showShortToast(this@UnLockCarActivity, "请先允许相机权限")
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })

    }
}