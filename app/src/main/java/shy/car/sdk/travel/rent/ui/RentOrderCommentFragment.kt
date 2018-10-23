package shy.car.sdk.travel.rent.ui

import android.Manifest
import android.app.Activity
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
import com.base.util.Phone
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
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentRentOrderCommentBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.presenter.RentOrderCommentPresenter


/**
 * create by LZ at 2018/07/04
 * 租车评价
 */
class RentOrderCommentFragment : XTBaseFragment(),
        CommonCallBack<JsonObject> {

    fun setDetail(detail: RentOrderDetail) {
        presenter.detail = detail
    }

    override fun onSuccess(t: JsonObject) {
        activity?.let {
            ToastManager.showLongToast(it, "提交成功，感谢您对我们服务的支持与认可")
        }
        finish()
    }

    override fun onError(e: Throwable) {

    }

    lateinit var binding: FragmentRentOrderCommentBinding
    lateinit var presenter: RentOrderCommentPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = RentOrderCommentPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_order_comment, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rating.setOnStarTouchListener {
            presenter.level = it
        }
    }

    fun upload() {
        presenter.uploadComment()
    }

    fun goAlbum() {
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
                                PickConfig.with(it)
                                        .pickMode(PickConfig.MODE_SINGLE_PICK)
                                        .isneedcamera(true)
                                        .start()
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
        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                activity?.let {
                    ProgressDialog.showLoadingView(it)
                }
                Observable.create<String> {
                    val path = ImageUtil.saveBitmapToSD(BitmapFactory.decodeFile(imgs[0]), Environment.getExternalStorageDirectory().absolutePath + "/cache", 35)
                    it.onNext(path)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            presenter.photo.set(it)
                            activity?.let {
                                ProgressDialog.hideLoadingView(it)
                            }
                        }, {
                            activity?.let {
                                ProgressDialog.hideLoadingView(it)
                            }
                            it.printStackTrace()
                        })


            }
        }
    }

}