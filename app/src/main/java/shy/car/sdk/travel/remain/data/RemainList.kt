package shy.car.sdk.travel.remain.data

import com.google.gson.annotations.SerializedName

data class RemainList(
    @SerializedName("id") var id: String,
    @SerializedName("money") var money: Double,
    @SerializedName("type") var type: Int,
    @SerializedName("type_text") var typeText: String,
    @SerializedName("created_at") var createdAt: String
)