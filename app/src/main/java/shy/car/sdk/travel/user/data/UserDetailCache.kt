package shy.car.sdk.travel.user.data

import com.google.gson.annotations.SerializedName

data class UserDetailCache(
        @SerializedName("name") var name: String,
        @SerializedName("avatar") var avatar: String,
        @SerializedName("sex") var sex: Int,
        @SerializedName("rank") var rank: Int,
        @SerializedName("rank_text") var rankText: String,
        @SerializedName("type") var type: Int,
        @SerializedName("type_text") var typeText: String,
        @SerializedName("is_identity_auth") var isIdentityAuth: Int,
        @SerializedName("is_deposit") var isDeposit: Int,
        @SerializedName("balance") var balance: Double,
        @SerializedName("score") var score: Int,
        @SerializedName("phone") var phone: String,
        @SerializedName("bank_card_num") var bankCardNum: Int,
        @SerializedName("coupon_num") var couponNum: Int,
        @SerializedName("city") var city: String,
        @SerializedName("profession") var profession: String,
        @SerializedName("birthday") var birthday: String,
        @SerializedName("nickname") var nickname: String,
        @SerializedName("scope") var scope: String
)

