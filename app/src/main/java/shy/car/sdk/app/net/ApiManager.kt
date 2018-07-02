package shy.car.sdk.app.net


import com.base.network.retrofit.BaseApiManager
import com.base.network.retrofit.BaseRetrofitInterface
import com.base.util.StringUtils
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * 统一接口管理
 * 所有接口调用都放在这里使用
 * 使用方式ApiManager.getInstance().method()
 */
class ApiManager(baseUrl: String, interceptor: BaseInterceptor, listener: BaseRetrofitInterface, c: Class<ApiInterface>) : BaseApiManager<ApiInterface>(baseUrl, interceptor, listener, c) {

    /**
     * 单例
     */
    companion object {
        //获取ApiManager的单例
        private lateinit var ins: ApiManager

        fun getInstance(): ApiManager {
            return ins
        }

        fun init(baseUrl: String, interceptor: BaseInterceptor, listener: BaseRetrofitInterface, c: Class<ApiInterface>) {
            ins = ApiManager(baseUrl, interceptor, listener, c)
        }

        fun toRequestBody(value: String? = null): RequestBody? {
            return if (StringUtils.isEmpty(value)) {
                null
            } else
                RequestBody.create(MediaType.parse("text/plain"), value)
        }
    }
}