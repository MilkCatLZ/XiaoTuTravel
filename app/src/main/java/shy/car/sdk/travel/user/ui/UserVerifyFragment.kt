package shy.car.sdk.travel.user.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.databinding.FragmentVerifyUserBinding
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserState
import shy.car.sdk.travel.user.dialog.UserVerifyDriveCardDialog
import shy.car.sdk.travel.user.dialog.UserVerifyIDCardHintDialog
import shy.car.sdk.travel.user.dialog.UserVerifyIDCardWithPersonDialog
import shy.car.sdk.travel.user.presenter.UserVerifyPresenter


/**
 * create by LZ at 2018/05/24
 * 验证用户
 */
class UserVerifyFragment : XTBaseFragment(),
        UserVerifyPresenter.SubmitListener,
        DialogInterface.OnDismissListener {
    override fun alreadyUpLoad(e: Throwable) {
        eventBusDefault.post(RefreshUserInfo())
        finish()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        eventBusDefault.post(RefreshUserInfo())
        finish()
    }

    override fun onUploadSuccess() {
        var dialog = UserVerifySubmitSuccessDialogFragment()
        dialog.listener = this
        dialog.show(childFragmentManager, "dialog_user_verify_success")

    }

    override fun onUploadError(e: Throwable) {

    }

    lateinit var binding: FragmentVerifyUserBinding
    lateinit var presenter: UserVerifyPresenter


    /**
     * 标识是从身份证还是驾驶证打开选择图片
     */
    private var idFront: Boolean = false
    private var idBack: Boolean = false
    private var drive: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = UserVerifyPresenter(it)
            presenter.listener = this
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_user, null, false)
        binding.fragment = this
        binding.presenter = presenter
        binding.user = User.instance
        presenter.name.set(User.instance.name)
        return binding.root
    }


    fun onIDPicFrontClick() {
        if (User.instance.identityAuth == UserState.UserIdentityAuth.NoIdentity) {
            idFront = true
            idBack = false
            drive = false
            val dialog = UserVerifyIDCardHintDialog()
            dialog.listener = object : UserVerifyIDCardHintDialog.OnConfirmClick {
                override fun onClick() {
                    openAlbum()
                }
            }
            dialog.show(childFragmentManager, "dialog_hint_id_card")
        }
    }

    fun onIDPicBackClick() {
        if (User.instance.identityAuth == UserState.UserIdentityAuth.NoIdentity) {
            idFront = false
            idBack = true
            drive = false
            val dialog = UserVerifyIDCardWithPersonDialog()
            dialog.listener = object : UserVerifyIDCardWithPersonDialog.OnConfirmClick {
                override fun onClick() {
                    openAlbum()
                }
            }
            dialog.show(childFragmentManager, "dialog_hint_id_card_with_person")
        }
    }

    fun onDriveVerifyImgClick() {
        if (User.instance.identityAuth == UserState.UserIdentityAuth.NoIdentity) {
            idFront = false
            idBack = false
            drive = true
            val dialog = UserVerifyDriveCardDialog()
            dialog.listener = object : UserVerifyDriveCardDialog.OnConfirmClick {
                override fun onClick() {
                    openAlbum()
                }
            }
            dialog.show(childFragmentManager, "dialog_hint_drive_card")
        }
    }

    private fun openAlbum() {
        checkPermission()
    }

    private fun checkPermission() {
        activity?.let {

            val per = RxPermissions(it)
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
                                activity?.let {
                                    PickConfig.with(it)
                                            .pickMode(if (idBack) PickConfig.MODE_SINGLE_PICK else PickConfig.MODE_TAKE_PHOTO)
                                            .isneedcamera(true)
                                            .start()
                                }
                            } else {
                                ToastManager.showShortToast(it, "请先允许相机权限")
                            }
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                ProgressDialog.showLoadingView(activity!!)
                Observable.create<String> {
                    val path = ImageUtil.saveBitmapToSD(BitmapFactory.decodeFile(imgs[0]), Environment.getExternalStorageDirectory().absolutePath + "/cache", 35)
                    it.onNext(path)
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            when {
                                idFront -> {
                                    presenter.frontImagePath.set(it)
                                }
                                idBack -> {
                                    presenter.backImagePath.set(it)
                                }
                                drive -> {
                                    presenter.driveImagePath.set(it)
                                }
                            }
                            ProgressDialog.hideLoadingView(activity!!)
                        }, {
                            ProgressDialog.hideLoadingView(activity!!)
                            it.printStackTrace()
                        })


            }
        }
    }

    fun onSubmitClick() {
        presenter.submit()
    }

}