package shy.car.sdk.travel.setting.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.base.ProgressDialog
import com.base.umeng.UmengShareManager
import com.base.util.Log
import com.base.util.ToastManager
import com.umeng.socialize.ShareAction
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityShareBinding
import shy.car.sdk.travel.setting.data.ShareDetail

/**
 * create by Sharon at 2018/07/07
 * 分享
 */
@Route(path = RouteMap.Share)
class ShareActivity : XTBaseActivity() {

    /**
     * 友盟分享的回调
     */
    var listener = object : UmengShareManager() {
        override fun onResult(p0: SHARE_MEDIA?) {
            ToastManager.showShortToast(this@ShareActivity, "分享成功")
        }

        override fun onCancel(p0: SHARE_MEDIA?) {
            Log.d("share", "onCancel")
        }

        override fun onError(p0: SHARE_MEDIA?, p1: Throwable) {
            val message = p1.message

            if (message != null && message.contains("2008"))
                when (p0?.name) {
                    "WEIXIN", "WEIXIN_CIRCLE" -> ToastManager.showShortToast(this@ShareActivity, "请先安装微信客户端")
                }
        }

        override fun onStart(p0: SHARE_MEDIA?) {

        }
    }

    lateinit var binding: ActivityShareBinding
    lateinit var detail: ShareDetail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share)
        binding.ac = this
        ProgressDialog.showLoadingView(this)
        getShareDetail()
    }

    private fun getShareDetail() {
        val observable = ApiManager.getInstance()
                .api.getShareDetail()
        val observer = object : Observer<ShareDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(detail: ShareDetail) {
                ProgressDialog.hideLoadingView(this@ShareActivity)
                this@ShareActivity.detail = detail
                binding.detail = detail
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(this@ShareActivity)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun wechat() {
        ShareAction(this)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(getUmengMedia())
                .setCallback(listener)
                .share()
    }

    fun wechatFriend() {
        ShareAction(this@ShareActivity)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(getUmengMedia())
                .setCallback(listener)
                .share()
    }

    /**
     * 分享图片
     */
    private fun getUmengMedia(): UMWeb {
        val web = UMWeb(detail.urls.android)
        web.title = detail.title
        web.description = detail.content
        val thumb = UMImage(this, R.mipmap.ic_launcher)
        web.setThumb(thumb)
        return web
    }

}