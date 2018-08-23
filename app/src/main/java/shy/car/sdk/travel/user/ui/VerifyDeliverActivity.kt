package shy.car.sdk.travel.user.ui

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
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
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityVerifyDeliverBinding
import shy.car.sdk.travel.take.presenter.VerifyDeliverPresenter

/**
 * 认证司机资格证
 */
@Route(path = RouteMap.DriveVerify)
class VerifyDeliverActivity : XTBaseActivity(),
        VerifyDeliverPresenter.CallBack,
        DialogInterface.OnDismissListener {
    override fun onDismiss(dialog: DialogInterface?) {
        finish()
    }

    override fun upLoadSuccess(t: JsonObject) {
        var dialog = UserVerifySubmitSuccessDialogFragment()
        dialog.listener = this
        dialog.show(supportFragmentManager, "dialog_user_verify_success")
    }

    lateinit var binding: ActivityVerifyDeliverBinding
    lateinit var presenter: VerifyDeliverPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_deliver)
        presenter = VerifyDeliverPresenter(this, this)

        binding.ac = this
        binding.presenter = presenter
    }

    fun upload() {
        if (presenter.photo.get().isNullOrEmpty()) {
            ToastManager.showShortToast(this, "请拍摄货运资格证照片")
        } else
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
                            PickConfig.with(this@VerifyDeliverActivity)
                                    .pickMode(PickConfig.MODE_TAKE_PHOTO)
                                    .isneedcamera(true)
                                    .start()
                        } else {
                            ToastManager.showShortToast(this@VerifyDeliverActivity, "请先允许相机权限")
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