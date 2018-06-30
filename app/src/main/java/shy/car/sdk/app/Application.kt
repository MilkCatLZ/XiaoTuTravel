package shy.car.sdk.app

import android.content.Context
import android.support.multidex.MultiDex
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.alipay.sdk.app.EnvUtils
import com.base.app.BaseApplication
import com.base.base.ProgressDialog
import com.base.location.AmapLocationManager
import com.base.location.Location
import com.base.util.SPCache
import com.base.util.crash.CrashHandler
import com.github.promeg.pinyinhelper.Pinyin
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import mall.lianni.alipay.Alipay
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Response
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.data.LoginOutOfDateException
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.app.eventbus.UserLogout
import shy.car.sdk.app.net.ApiInterface
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.net.BaseInterceptor
import shy.car.sdk.app.net.XTRetrofitInterface
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.login.ui.LoginDialogFragment
import shy.car.sdk.travel.login.ui.VerifyDialogFragment
import shy.car.sdk.travel.user.data.User

class Application : BaseApplication() {


    var device_token: String = ""
    var location: CurrentLocation = CurrentLocation()
    lateinit var api: IWXAPI
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            CrashHandler.getInstance()
                    .init(this, CrashHandler.CrashCallBack {
                        it.printStackTrace()
                    })
        initNetWork()
        initRouter()
        initPinYin()
        initAmap()
        initUserCache()
        EventBus.builder()
                .build()
        EventBus.getDefault()
                .register(this)


        initPay()
        if (BuildConfig.DEBUG) {
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    private fun initPay() {
        Alipay.Init(this)
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAppID, true)
    }

    private fun initUserCache() {
        User.initUserCache(this)
        if (User.instance.login) {
            User.instance.getUserDetail(this)
        }
    }

    private fun initAmap() {
        AmapLocationManager.getInstance()
                .init(this)
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

    private val InterfaceVersion = "v1"

    /**
     * 初始化网络
     */
    private fun initNetWork() {
        val interceptor = BaseInterceptor(this)
        val iterator = XTRetrofitInterface(this)
        val cookieJar = object : CookieJar {
            override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {

            }

            override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                val cookies = ArrayList<Cookie>()
                val token = SPCache.getObject<String>(this@Application,
                        "set-cookie",
                        String::class.java)
                if (token != null) {
                    var cookie = Cookie.parse(url, token)
                    cookies.add(cookie!!)
                }
                return cookies;
            }

        }
        ApiManager.init(BuildConfig.Host + "$InterfaceVersion/",
                interceptor,
                iterator,
                ApiInterface::class.java)
    }

    var postcard: Postcard? = null
    var callback: InterceptorCallback? = null
    var cityList: List<CurrentLocation> = ArrayList<CurrentLocation>()

    fun startLoginDialog(postcard: Postcard?, callback: InterceptorCallback?, listener: onLoginDismiss? = null) {
        try {
            this.postcard = postcard
            this.callback = callback
            val dialogFragment = ARouter.getInstance().build(RouteMap.Login).greenChannel().navigation() as LoginDialogFragment
            dialogFragment.listener = listener
            dialogFragment.show(activityList[0].supportFragmentManager, "fragment_login_dialog")
        } catch (e: Exception) {
        }
    }

    fun startVerifyDialog(phone: String, interval: Int = 60, listener: onLoginDismiss? = null) {
        try {
            val dialogFragment = ARouter.getInstance().build(RouteMap.Verify)
                    .greenChannel()
                    .withString(ParamsConstant.String1, phone)
                    .withInt(ParamsConstant.Int1, interval)
                    .navigation() as VerifyDialogFragment
            dialogFragment.dismissListener = listener
            dialogFragment.show(activityList[0].supportFragmentManager, "fragment_verify_dialog")
        } catch (e: Exception) {
        }

    }

    fun startVerifyDialogVerify(phone: String, interval: Int = 60, listener: onLoginDismiss? = null, verify: String) {
        try {
            val dialogFragment = ARouter.getInstance().build(RouteMap.Verify)
                    .greenChannel()
                    .withString(ParamsConstant.String1, phone)
                    .withInt(ParamsConstant.Int1, interval)
                    .withString(ParamsConstant.String2, verify)
                    .navigation() as VerifyDialogFragment
            dialogFragment.dismissListener = listener
            dialogFragment.show(activityList[0].supportFragmentManager, "fragment_verify_dialog")
        } catch (e: Exception) {
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(success: LoginSuccess) {
        ProgressDialog.showLoadingView(activityList[0])
        if (User.instance.login)
            User.instance.getUserDetail(this, object : User.OnGetUserDetailSuccess {
                override fun onError() {
                    ProgressDialog.hideLoadingView(activityList[0])
                }

                override fun onSuccess() {
                    ProgressDialog.hideLoadingView(activityList[0])
                    if (postcard != null && callback != null) {
                        callback?.onContinue(postcard)
                        postcard = null
                        callback = null
                    }
                }
            })

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUserInfo(refreshUserInfo: RefreshUserInfo?) {
        if (User.instance.login)
            User.instance.getUserDetail(this)
    }

    fun changeCurrentLocation(l: Location) {
        location.copy(l)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun tokenOutDate(oudate: LoginOutOfDateException) {
        if (activityList.size >= 2)
            for (i in activityList.size - 1 downTo 1) {
                activityList[i].finish()
                activityList.removeAt(i)
            }
        logout()
    }

    var disposable: Disposable? = null
    fun logout() {
//        User.logout(this@Application)
        val observable = ApiManager.getInstance()
                .api.logout()

        var observer = object : Observer<Response<Void>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: Response<Void>) {
                User.logout(this@Application)
                EventBus.getDefault().post(UserLogout())
                ApiManager.getInstance()
                        .clearCache()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                disposable?.dispose()
                User.logout(this@Application)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun goHome() {
        for (i in (activityList.size - 1) downTo 1) {
            activityList[i].finish()
            activityList.removeAt(i)
        }
    }

}