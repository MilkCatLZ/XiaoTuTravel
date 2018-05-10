package shy.car.sdk.app.net


import android.content.Context
import com.base.net.BaseInterceptor
import com.base.util.Log
import com.base.util.StringUtils
import com.base.util.Version
import org.xutils.common.util.KeyValue
import org.xutils.common.util.MD5
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.travel.user.data.User
import java.util.*
import kotlin.collections.ArrayList

/**
 * 该类是retrofit拦截器，用于给每个请求的链接上 添加公共参数
 */
class BaseInterceptor(val app: Context) : BaseInterceptor() {

    private var APPID: String = ""
    /**
     * 本平台用于签名的secret，这里存的是密文
     * 需要通过阿里聚安全解密
     */
    private var SCERET: String = ""
    /**
     * 阿里聚安全的key
     * http://jaq.alibaba.com/gc/sdk/sdk_main.htm?spm=a313e.7913620.11111111.9.4c80ab63aYr104#/sdkDownload?itemId=b6eaf906-1b7b-49ed-ac92-7e44038ddf9b
     * 如果找不到请找负责人
     * 目前用的是39****06@qq.com的支付宝
     */
    private var KEY: String = ""

    private val ACCESS_TOKEN = "access_token"
    private val TIMESTAMP = "timestamp"
    private val APPID1 = "appid"
    private val SIGN = "sign"

//    private val securityCipher: SecurityCipher = SecurityCipher(app)

    init {
        SCERET = app.getString(R.string.str_afjjelldfjlkjd_)
        KEY = app.getString(R.string.str_sdlkjcviiglldldfjlkjlksjdf)
        APPID = app.getString(R.string.app_id)
    }

    /**
     * 截取url 重新拼接公共参数
     */
    override fun postUrl(url: String, rootParams: HashMap<String, String>): String {
        getSign(url, rootParams)
        return createUrl(url, rootParams)
    }

    override fun getUrl(url: String, rootParams: HashMap<String, String>): String {
        getSign(url, rootParams)
        return createUrl(url, rootParams)
    }

    /**
     * @param params
     *
     * @return 返回签名字符串
     */
    private fun getSign(url: String, params: HashMap<String, String>): String {
        addParams(params)

        val list = sort(toKeyValueList(params))
        //这里是完整的额url,所以要先获得先对的
        val result = getSignString(list, getRelativeUrl(url))
        params[SIGN] = result
        Log.d("getSign", result)
        return result
    }

    private fun toKeyValueList(params: HashMap<String, String>): ArrayList<KeyValue> {
        val list = ArrayList<KeyValue>()
        params.mapValues { (key, value) -> list.add(KeyValue(key, value)) }
        return list
    }

    /**
     * 添加参数
     *
     * @param params
     */
    private fun addParams(params: HashMap<String, String>) {
        if (!StringUtils.isEmpty(User.instance
                        .access_token)) {
            params[ACCESS_TOKEN] = User.instance
                    .access_token
        }
        params[TIMESTAMP] = (Calendar.getInstance().timeInMillis / 1000).toString()
        params[APPID1] = APPID
        params["version"] = Version.getVersion(app)
        params["Content-Type"] = "application/json;charset=UTF-8"

    }

    /**
     * 排序
     *
     * @param list
     *
     * @return 返回排好序的列表
     */
    private fun sort(list: List<KeyValue>): List<KeyValue> {
        Collections.sort(list) { lhs, rhs -> lhs.key.compareTo(rhs.key) }
        return list
    }

    /**
     * @param list
     * @param url
     *
     * @return 因为access_token不能转码，所以只能在之前提交加到url中，但是这样会重复，所有这里重新截取?
     */
    private fun getSignString(list: List<KeyValue>, url: String): String {
        var stringBuilder: StringBuilder = try {
            StringBuilder("_url=" + url.substring(0, url.indexOf("?")) + "&")
        } catch (e: Exception) {
            StringBuilder("_url=$url&")
        }

        val size = list.size
        for (i in 0 until size) {
            val keyValue = list[i]
            stringBuilder.append(keyValue.key)
            stringBuilder.append("=")
            stringBuilder.append(keyValue.value)
            if (i != size - 1)
                stringBuilder.append("&")
        }


//        try {
        stringBuilder.append(SCERET /*securityCipher.decryptString(SCERET, KEY)*/)
//        } catch (e: JAQException) {
//            Log.e("JAQException=====", "error======================" + e.errorCode)
//        }

        return MD5.md5(stringBuilder!!.toString())
                .toUpperCase()
    }

    private fun getRelativeUrl(url: String): String {
        val strings = url.split(BuildConfig.Host.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (strings.size == 2) {
            "/" + strings[1]
        } else url
    }
}
