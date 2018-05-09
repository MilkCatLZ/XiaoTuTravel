package shy.car.sdk.app.util


import com.base.util.Log


/**
 * Created by LZ on 2017/8/15.
 */

object PassWordEncode {
    fun encodePass(pass: String): String {
        //对原文进行一次base64得到base64字符串，获取该字符串的byte数组
        val bytes = mall.lianni.alipay.Base64.encode(pass.toByteArray()).toByteArray()

        //把这个base64字符串的第一位和最后一位换位置
        val x1 = bytes[0]
        bytes[0] = bytes[bytes.size - 1]
        bytes[bytes.size - 1] = x1

        //再进行一次base64，上传
        val s = mall.lianni.alipay.Base64.encode(bytes)
        Log.i("PassWordEncode", "encodePass---------$s")
        return s
    }
}
