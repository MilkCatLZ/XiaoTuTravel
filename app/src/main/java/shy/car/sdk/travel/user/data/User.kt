package shy.car.sdk.travel.user.data


import android.content.Context
import com.base.util.AesUtil
import com.base.util.SPCache
import com.base.util.StringUtils
import com.google.gson.Gson
import shy.car.sdk.BR


/**
 * create by LZ at 2018/05/16
 * 用户信息
 */
class User private constructor() : UserBase() {
    //endregion


    companion object {
        val instance = User()
        internal val UserKey = "app:users"
        private val key = "qpelakfjgjfkdlsd"

        //region 逻辑相关

        /**
         * 初始化缓存中的用户信息
         *
         * @param context
         */
        fun initUserCache(context: Context) {
            var uJson: String? = null
            try {

                uJson = AesUtil.decrypt(key, SPCache.getObject<Any>(context, UserKey, String::class.java)!!.toString())
            } catch (e: Exception) {

            }

            if (StringUtils.isNotEmpty(uJson)) {
                try {
                    val user = Gson().fromJson(uJson, User::class.java)
                    copyUser(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }


        }

        /**
         * 更新缓存中的登录状态
         *
         * @param context
         */
        fun saveUserState(context: Context) {
            var uJson: String? = null
            try {
                uJson = AesUtil.encrypt(key, Gson().toJson(instance))
                SPCache.saveObject(context, UserKey, uJson)
                User.instance.notifyPropertyChanged(BR.login)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun logout(context: Context) {
            val user = User()
            var uJson: String? = null
            try {
                //            uJson = KeyStoreUtil.encryptString(context, JSONObject.toJSONString(user));
                //            uJson = JSONObject.toJSONString(user);
                uJson = AesUtil.encrypt(key, Gson().toJson(user))
                SPCache.saveObject(context, UserKey, uJson)
                copyUser(user)
                //            PushManager.deleteAlias(context, user.getUid());
                User.instance.notifyPropertyChanged(BR.login)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        /**
         * 复制用户信息到主用户
         *
         * @param dis
         */
        private fun copyUser(dis: User?) {
            if (dis == null) {
                return
            }
            User.instance.access_token = dis.access_token
            User.instance.avatar = dis.avatar
            User.instance.loginType = dis.loginType
            User.instance.nickName = dis.nickName
            User.instance.phone = dis.phone
            User.instance.expiresIn = dis.expiresIn
            User.instance.password = dis.password
            User.instance.paymentPassword = dis.paymentPassword
            User.instance.status = dis.status
            User.instance.msgRemind = dis.msgRemind
            User.instance.loginTime = dis.loginTime
            User.instance.refreshToken = dis.refreshToken
            User.instance.scope = dis.scope
            User.instance.tokenType = dis.tokenType
        }

    }


}
