package shy.car.sdk.travel.user.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.util.ToastManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wq.photo.widget.PickConfig
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentVerifyUserBinding
import shy.car.sdk.travel.user.presenter.VerifyUserPresenter


/**
 * create by LZ at 2018/05/24
 * 验证用户
 */
class VerifyUserFragment : XTBaseFragment() {

    lateinit var binding: FragmentVerifyUserBinding
    lateinit var presenter: VerifyUserPresenter


    /**
     * 标识是从身份证还是驾驶证打开选择图片
     */
    private var idFront: Boolean = false
    private var idBack: Boolean = false
    private var drive: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = VerifyUserPresenter(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_user, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }


    fun onIDPicFrontClick() {
        idFront = true
        idBack = false
        drive = false
        openAlbum()
    }

    fun onIDPicBackClick() {
        idFront = false
        idBack = true
        drive = false
        openAlbum()

    }

    fun onDriveVerifyImgClick() {
        idFront = false
        idBack = false
        drive = true
        openAlbum()
    }

    private fun openAlbum() {
        checkPermission()
    }

    private fun checkPermission() {
        activity?.let {

            val per = RxPermissions(it)
            per.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
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
                                            .pickMode(PickConfig.MODE_SINGLE_PICK)
                                            .isneedcamera(true)
                                            .start()
                                }
                            }else{
                                ToastManager.showShortToast(it, "请先允许相机权限");
                            }
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
            per.request(Manifest.permission.CAMERA)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                when {
                    idFront -> {
                        presenter.frontImagePath.set(imgs[0])
                    }
                    idBack -> {
                        presenter.backImagePath.set(imgs[0])
                    }
                    drive -> {
                        presenter.driveImagePath.set(imgs[0])
                    }
                }
            }
        }
    }

    fun onSubmitClick() {
        presenter.submit()
    }

}