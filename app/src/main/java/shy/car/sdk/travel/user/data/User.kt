package shy.car.sdk.travel.user.data


import android.content.Context

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.annotation.JSONField
import com.base.util.AesUtil
import com.base.util.SPCache
import com.base.util.StringUtils
import shy.car.sdk.BR


/**
 * Created by Syokora on 2016/8/18.
 * 用户类，单实例
 */
class User private constructor() : UserBase() {
    //endregion


    object Type {
        /**
         * 手机登陆类型，=1
         */
        var PINGTAI = 1
        /**
         * 微信登陆类型，=2
         */
        var DIANPU = 2

    }

    companion object {
        @get:JSONField(serialize = false)
        val instance = User()
        internal val LIANNI_USER = "lianni:user"
        private val key = "qpelakfjgjfkdlsd"

        //region 逻辑相关

        /**
         * 初始化缓存中的用户信息
         *
         * @param context
         */
        @JSONField(serialize = false)
        fun initUserCache(context: Context) {
            var uJson: String? = null
            try {

                uJson = AesUtil.decrypt(key, SPCache.getObject<Any>(context, LIANNI_USER, String::class.java)!!.toString())
            } catch (e: Exception) {

            }

            if (StringUtils.isNotEmpty(uJson)) {
                try {
                    val user = JSON.parseObject(uJson, User::class.java)
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
        @JSONField(serialize = false)
        fun saveUserState(context: Context) {
            var uJson: String? = null
            try {
                //            uJson = KeyStoreUtil.encryptString(context, JSONObject.toJSONString(user));
                //            uJson = JSONObject.toJSONString(user);
                uJson = AesUtil.encrypt(key, JSONObject.toJSONString(instance))
                SPCache.saveObject(context, LIANNI_USER, uJson)
                User.instance.notifyPropertyChanged(BR.login)
            } catch (e: Exception) {

            }

        }


        @JSONField(serialize = false)
        fun logout(context: Context) {
            val user = User()
            var uJson: String? = null
            try {
                //            uJson = KeyStoreUtil.encryptString(context, JSONObject.toJSONString(user));
                //            uJson = JSONObject.toJSONString(user);
                uJson = AesUtil.encrypt(key, JSONObject.toJSONString(user))
                SPCache.saveObject(context, LIANNI_USER, uJson)
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
        @JSONField(serialize = false)
        private fun copyUser(dis: User?) {
            if (dis == null) {
                return
            }
            User.instance.access_token = dis.access_token
            User.instance.avatar = dis.avatar
            //        User.user.setBindWeixin(dis.getBindWeixin());
            User.instance.loginType = dis.loginType
            User.instance.nickName = dis.nickName
            User.instance.phone = dis.phone
            if (dis.uid != 0)
                User.instance.uid = dis.uid
            User.instance.expiresIn = dis.expiresIn
            //add on 2017.6.20 by lz
            User.instance.type = dis.type
            User.instance.password = dis.password
            User.instance.paymentPassword = dis.paymentPassword
            User.instance.status = dis.status
            User.instance.deliveryName = dis.deliveryName
            User.instance.msgRemind = dis.msgRemind
            User.instance.loginTime = dis.loginTime
            User.instance.superiorLevel = dis.superiorLevel
            User.instance.superiorName = dis.superiorName
            User.instance.superiorPhone = dis.superiorPhone
            User.instance.jobLevel = dis.jobLevel
            User.instance.workTime = dis.workTime
            User.instance.partner = dis.partner
        }


        fun updateDetails(user: User) {
            if (user.uid != 0)
                User.instance.uid = user.uid
            User.instance.avatar = user.avatar
            User.instance.nickName = user.nickName
            User.instance.phone = user.phone
            User.instance.password = user.password
            User.instance.paymentPassword = user.paymentPassword
            User.instance.status = user.status
            User.instance.deliveryName = user.deliveryName
            User.instance.type = user.type
            User.instance.msgRemind = user.msgRemind
            User.instance.superiorLevel = user.superiorLevel
            User.instance.superiorName = user.superiorName
            User.instance.superiorPhone = user.superiorPhone
            User.instance.jobLevel = user.jobLevel
            User.instance.workTime = user.workTime
            User.instance.partner = user.partner
        }
    }


}
