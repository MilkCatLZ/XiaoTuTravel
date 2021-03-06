package shy.car.sdk.travel.user.data


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.base.util.StringUtils

import com.google.gson.annotations.SerializedName

import shy.car.sdk.BR
import shy.car.sdk.app.constant.ParamsConstant.City


/**
 * Created by LZ on 2016/8/26.
 * 用户
 */
open class UserBase : BaseObservable() {

    /**
     * uid : 10314
     * phone : 18577845220
     * bind_weixin : 0
     */
    @SerializedName(PHONE)
    @get:Bindable
    var phone: String = ""
        set(phone) {
            if ("0" == phone) {
                field = ""
            } else {
                field = phone
            }
            notifyPropertyChanged(BR.phone)
        }

    @SerializedName(ACCESS_TOKEN)
    var accessToken: String = ""

    @SerializedName(REFRESH_TOKEN)
    var refreshToken: String = ""


    @SerializedName("max_cancel_num")
    @get:Bindable
    var maxCancelNum: Int = 5
        set(value) {
            field = value
            notifyPropertyChanged(BR.maxCancelNum)
        }
    @SerializedName(Name)
    @get:Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @SerializedName(AVATAR)
    @get:Bindable
    var avatar: String = ""
        set(avatar) {
            field = avatar
            notifyPropertyChanged(BR.avatar)
        }
    @SerializedName(Birthday)
    @get:Bindable
    var birthday: String = ""
        set(birthday) {
            field = birthday
            notifyPropertyChanged(BR.birthday)
        }
    /**
     * 性别（0保密 1男 2女）
     */
    @SerializedName(SEX)
    @get:Bindable
    var sex: Int = 0
        set(sex) {
            field = sex
            notifyPropertyChanged(BR.sex)
        }
    /**
     * 用户等级
     */
    @SerializedName("rank")
    @get:Bindable
    var rank: Int = 0
        set(rank) {
            field = rank
            notifyPropertyChanged(BR.rank)
        }

    /**
     * 用户等级名称
     */
    @SerializedName("rank_text")
    @get:Bindable
    var rankText: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.rankText)
        }
    /**
     * 昵称
     */
    @SerializedName("nickname")
    @get:Bindable
    var nickName: String = ""
        set(nickName) {
            field = nickName
            notifyPropertyChanged(BR.nickName)
        }
    /**
     * 用户类型
     */
    @SerializedName("type")
    @get:Bindable
    var type: Int = 0
        set(type) {
            field = type
            notifyPropertyChanged(BR.type)
        }

    /**
     * 类型名称
     */
    @SerializedName("type_text")
    @get:Bindable
    var typeText: String = "个人用户"
        set(type_text) {
            field = type_text
            notifyPropertyChanged(BR.typeText)
        }

    /**
     * 认证状态（0未认证 1认证中 2已认证）
     */
    @SerializedName("is_identity_auth")
    @get:Bindable
    var identityAuth: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.identityAuth)
            notifyPropertyChanged(BR.isIdentityAuth)
            notifyPropertyChanged(BR.isDeposit)
        }
    /**
     * 认证状态（0未认证 1认证中 2已认证）
     */
    @SerializedName("is_driver_auth")
    @get:Bindable
    var driverAuth: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.isDriverAuth)
            notifyPropertyChanged(BR.driverAuth)
        }

    @Bindable
    fun getIsDriverAuth(): Boolean {
        return driverAuth == 2
    }

    @Bindable
    fun getIsIdentityAuth(): Boolean {
        return identityAuth == UserState.UserIdentityAuth.Identited
    }

    /**
     * 保证金
     */
    @SerializedName("deposit")
    @get:Bindable
    var deposit: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.deposit)
            notifyPropertyChanged(BR.isIdentityAuth)
            notifyPropertyChanged(BR.isDeposit)
        }

    /**
     * 保证金（0未缴 1已缴）
     */
    @SerializedName("is_deposit")

    var isDeposited: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.deposit)
            notifyPropertyChanged(BR.isIdentityAuth)
            notifyPropertyChanged(BR.isDeposit)
        }

    /**
     * 保证金（0未缴 1已缴）
     */
    @Bindable
    fun getIsDeposit(): Boolean {
        return isDeposited == 1
    }


    /**
     * 账户余额
     */
    @SerializedName("balance")
    @get:Bindable
    var balance: Double = 0.0
        set(balance) {
            field = balance
            notifyPropertyChanged(BR.balance)
        }
    /**
     * 用户积分
     */
    @SerializedName("score")
    @get:Bindable
    var score: Int = 0
        set(score) {
            field = score
            notifyPropertyChanged(BR.score)
        }
    /**
     * 银行卡数量
     */
    @SerializedName(BankCardNum)
    @get:Bindable
    var bankCardNum: Int = 0
        set(bank_card_num) {
            field = bank_card_num
            notifyPropertyChanged(BR.bankCardNum)
        }
    /**
     * 优惠劵数量
     */
    @SerializedName(CouponNum)
    @get:Bindable
    var couponNum: Int = 0
        set(coupon_num) {
            field = coupon_num
            notifyPropertyChanged(BR.couponNum)
        }


    @SerializedName(EXPIRES_IN)
    var expiresIn: Long = 0

    @SerializedName(Scope)
    @get:Bindable
    var scope: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.scope
            )
        }
    @SerializedName(Profession)
    @get:Bindable
    var profession: String? = null
        set(profession) {
            field = profession
            notifyPropertyChanged(BR.profession)
        }
    @SerializedName(City)
    @get:Bindable
    var city: String? = null
        set(city) {
            field = city
            notifyPropertyChanged(BR.city)
        }

    @SerializedName(TokenType)
    var tokenType: String = ""


    var loginTime: Long = 0

    @get:Bindable
    var login: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.login)
        }
        get() {
            return StringUtils.isNotEmpty(accessToken)
        }


    companion object {
        const val PHONE = "phone"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val AVATAR = "avatar"
        const val Name = "name"
        const val EXPIRES_IN = "expires_in"
        const val TokenType = "token_type"
        const val Scope = "scope"
        const val SEX = "sex"
        const val CouponNum = "coupon_num"
        const val BankCardNum = "bank_card_num"
        const val Profession = "profession"
        const val Birthday = "birthday"
    }

}
