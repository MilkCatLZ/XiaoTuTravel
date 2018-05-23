package shy.car.sdk.app

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.base.app.BaseApplication
import com.base.util.crash.CrashHandler
import com.github.promeg.pinyinhelper.Pinyin
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict
import com.lianni.mall.location.AmapLocationManager
import com.lianni.mall.location.Location
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.net.ApiInterface
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.net.BaseInterceptor
import shy.car.sdk.app.net.XTRetrofitInterface
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.login.ui.LoginDialogFragment
import shy.car.sdk.travel.login.ui.VerifyDialogFragment
import shy.car.sdk.travel.user.data.User

class Application : BaseApplication() {


    var device_token: String = ""
    var location: Location? = null

//    /**
//     * 高德地图定位回调
//     */
//    override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
//
//    }

    override fun onCreate() {
        super.onCreate()

//        CrashHandler.getInstance()
//                .init(this, null)

        initNetWork()
        initRouter()
        initPinYin()
        initAmap()
        initUserCache()
        EventBus.builder()
                .build()
        EventBus.getDefault()
                .register(this)

    }

    private fun initUserCache() {
        User.initUserCache(this)
    }

    private fun initAmap() {
        AmapLocationManager.init(this)
//        AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
//            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
//                Log.d("获取位置成功", "经纬度={${location.latitude},${location.longitude}}")
//                this@Application.location = location
//            }
//        })
    }

    private fun initPinYin() {
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(this)))
    }

    /**
     * 拦截器:shy.car.sdk.app.route.RouterInterceptor
     * 拦截需要登录才能进的页面
     */
    private fun initRouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()  // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
    }

    /**
     * 初始化网络
     */
    private fun initNetWork() {
        val interceptor = BaseInterceptor(this)
        val iterator = XTRetrofitInterface(this)
        ApiManager.init("https://www.baidu.com", interceptor, iterator, ApiInterface::class.java)
    }

    var postcard: Postcard? = null
    var callback: InterceptorCallback? = null
    fun startLoginDialog(postcard: Postcard?, callback: InterceptorCallback?) {
        try {
            this.postcard = postcard
            this.callback = callback
            val dialogFragment = ARouter.getInstance().build(RouteMap.Login).greenChannel().navigation() as LoginDialogFragment
            dialogFragment.show(activityList[0].supportFragmentManager, "fragment_login_dialog")
        } catch (e: Exception) {
        }
    }

    fun startVerifyDialog(phone: String) {
        try {
            val dialogFragment = ARouter.getInstance().build(RouteMap.Verify).greenChannel().withString("phone", phone).navigation() as VerifyDialogFragment
            dialogFragment.show(activityList[0].supportFragmentManager, "fragment_verify_dialog")
        } catch (e: Exception) {
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(success: LoginSuccess) {
        if (postcard != null && callback != null) {
            callback?.onContinue(postcard)
            postcard = null
            callback = null
        }
        User.saveUserState(this)
    }

}