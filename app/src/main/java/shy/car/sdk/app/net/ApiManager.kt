package shy.car.sdk.app.net


import com.base.net.BaseApiManager
import com.base.net.BaseRetrofitInterface

/**
 * 统一接口管理
 * 所有接口调用都放在这里使用
 * 使用方式ApiManager.instance.method()
 */
class ApiManager(baseUrl: String, interceptor: BaseInterceptor, listener: BaseRetrofitInterface, c: Class<ApiInterface>) : BaseApiManager<ApiInterface>(baseUrl, interceptor, listener, c) {

    /**
     * 单例
     */
    companion object {
        //获取ApiManager的单例
        lateinit var instance: ApiManager


        fun init(baseUrl: String, interceptor: BaseInterceptor, listener: BaseRetrofitInterface, c: Class<ApiInterface>) {
            instance = ApiManager(baseUrl, interceptor, listener, c)
        }

    }
}