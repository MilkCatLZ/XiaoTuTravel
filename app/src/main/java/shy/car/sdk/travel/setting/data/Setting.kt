package shy.car.sdk.travel.setting.data

import android.databinding.BaseObservable

import com.google.gson.annotations.SerializedName

class Setting : BaseObservable() {


    /**
     * order : {"day_max_cancel_num":5}
     * system : {"kf_tel":"400-100-456","kf_email":"123456789@qq.com"}
     * banks : [{"id":1,"name":"中国银行","logo":"http://47.106.88.148/assets/img/bank/bc.png"}]
     */

    @SerializedName("order")
    var order: OrderBean? = null
    @SerializedName("system")
    var system: SystemBean? = null
    @SerializedName("banks")
    var banks: List<BanksBean>? = null

    class OrderBean {
        /**
         * day_max_cancel_num : 5
         */

        @SerializedName("day_max_cancel_num")
        var dayMaxCancelNum: Int = 0
    }

    class SystemBean {
        /**
         * kf_tel : 400-100-456
         * kf_email : 123456789@qq.com
         */

        @SerializedName("kf_tel")
        var kfTel: String? = null
        @SerializedName("kf_email")
        var kfEmail: String? = null
    }

    class BanksBean {
        /**
         * id : 1
         * name : 中国银行
         * logo : http://47.106.88.148/assets/img/bank/bc.png
         */

        @SerializedName("id")
        var id: String? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("logo")
        var logo: String? = null
    }
}
