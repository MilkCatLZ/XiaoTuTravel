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

    fun copy(detail:UserDetailCache){
        instance.phone = detail.phone
        instance.name = detail.name
        instance.avatar = detail.avatar
        instance.type = detail.type
        instance.typeText = detail.typeText
        instance.identityAuth = detail.isIdentityAuth
        instance.deposit = detail.isDeposit
        instance.balance = detail.balance
        instance.bankCardNum = detail.bankCardNum
        instance.couponNum = detail.couponNum
        instance.rank = detail.rank
    }

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
                uJson = AesUtil.encrypt(key, Gson().toJson(user))
                SPCache.saveObject(context, UserKey, uJson)
                copyUser(user)
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
            instance.phone = dis.phone
            instance.accessToken = dis.accessToken
            instance.refreshToken = dis.refreshToken
            instance.name = dis.name
            instance.avatar = dis.avatar
            instance.type = dis.type
            instance.typeText = dis.typeText
            instance.identityAuth = dis.identityAuth
            instance.identityAuth = dis.identityAuth
            instance.balance = dis.balance
            instance.scope = dis.scope
            instance.bankCardNum = dis.bankCardNum
            instance.couponNum = dis.couponNum
            instance.rank = dis.rank
        }

    }


}
