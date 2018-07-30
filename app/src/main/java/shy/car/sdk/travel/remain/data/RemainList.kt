package shy.car.sdk.travel.remain.data

import com.google.gson.annotations.SerializedName
import shy.car.sdk.app.LNTextUtil

data class RemainList(
        @SerializedName("id") var id: String,
        @SerializedName("money") var money: Double,
        @SerializedName("type") var type: Int,
        @SerializedName("type_text") var typeText: String,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("balance") var balance: String
) {
    fun getRemainText(): String {

        return "余额:$balance"
    }

    fun getMoneyText(): String {
        return "${if (type == 1) "+" else "-"} ${LNTextUtil.getPriceText(money)}"
    }
}