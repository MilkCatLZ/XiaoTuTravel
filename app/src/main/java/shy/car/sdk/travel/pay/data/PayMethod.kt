package shy.car.sdk.travel.pay.data

import com.google.gson.annotations.SerializedName

data class PayMethod(
        @SerializedName("id") var id: Int,
        @SerializedName("name") var name: String,
        @SerializedName("logo") var logo: String
)