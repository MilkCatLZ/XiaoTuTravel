package shy.car.sdk.travel.setting.ui

import android.graphics.Bitmap
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.umeng.UmengShareManager
import com.base.util.Log
import com.base.util.ToastManager
import com.umeng.socialize.ShareAction
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

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
                    "QQ" -> ToastManager.showShortToast(this@ShareActivity, "请先安装QQ客户端")
                    "WEIXIN", "WEIXIN_CIRCLE" -> ToastManager.showShortToast(this@ShareActivity, "请先安装微信客户端")
                }
        }

        override fun onStart(p0: SHARE_MEDIA?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
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
        val web = UMWeb("")
        web.title = "Come on ！点击链接，解锁你的喝水新姿势【连你订水】"
        val thumb = UMImage(this, R.mipmap.ic_launcher)
        web.setThumb(thumb)
        web.description = "Come on ！点击链接，解锁你的喝水新姿势【连你订水】"


        return web
    }

    /**
     * 分享图片
     */
    private fun getUmengMedia(result: Bitmap?): UMImage {
        val image = UMImage(this, result)
        return image
    }

}