package shy.car.sdk.travel.setting.data

import android.databinding.BaseObservable

import com.google.gson.annotations.SerializedName
import shy.car.sdk.travel.bank.data.BankType

class Setting : BaseObservable() {


    /**
     * order : {"day_max_cancel_num":5}
     * system : {"kf_tel":"400-100-456","kf_email":"123456789@qq.com"}
     * banks : [{"id":1,"name":"中国银行","logo":"http://47.106.88.148/assets/img/bank/bc.png"},{"id":2,"name":"工商银行","logo":"http://47.106.88.148/assets/img/bank/icbc.png"},{"id":3,"name":"建设银行","logo":"http://47.106.88.148/assets/img/bank/cbc.png"},{"id":4,"name":"农业银行","logo":"http://47.106.88.148/assets/img/bank/abc.png"},{"id":5,"name":"招商银行","logo":"http://47.106.88.148/assets/img/bank/cmb.png"},{"id":6,"name":"光大银行","logo":"http://47.106.88.148/assets/img/bank/ceb.png"}]
     * htmls : {"about":"http://47.106.88.148/v1/html/about.html","users_agreement":"http://47.106.88.148/v1/html/agreement/users.html","freight_agreement":"http://47.106.88.148/v1/html/agreement/freight.html","charge_agreement":"http://47.106.88.148/v1/html/agreement/charge.html","return_car_agreement":"http://47.106.88.148/v1/html/agreement/return_car.html","register_agreement":"http://47.106.88.148/v1/html/agreement/register.html"}
     */

    @SerializedName("order")
    var order: OrderBean? = null
    @SerializedName("system")
    var system: SystemBean? = null
    @SerializedName("htmls")
    var htmls: HtmlsBean? = null
    @SerializedName("banks")
    var banks: List<BankType>? = null

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

    class HtmlsBean {
        /**
         * about : http://47.106.88.148/v1/html/about.html
         * users_agreement : http://47.106.88.148/v1/html/agreement/users.html
         * freight_agreement : http://47.106.88.148/v1/html/agreement/freight.html
         * charge_agreement : http://47.106.88.148/v1/html/agreement/charge.html
         * return_car_agreement : http://47.106.88.148/v1/html/agreement/return_car.html
         * register_agreement : http://47.106.88.148/v1/html/agreement/register.html
         */

        @SerializedName("about")
        var about: String? = null
        @SerializedName("users_agreement")
        var usersAgreement: String? = null
        @SerializedName("freight_agreement")
        var freightAgreement: String? = null
        @SerializedName("charge_agreement")
        var chargeAgreement: String? = null
        @SerializedName("return_car_agreement")
        var returnCarAgreement: String? = null
        @SerializedName("register_agreement")
        var registerAgreement: String? = null
    }


}
