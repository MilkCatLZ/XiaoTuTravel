package shy.car.sdk.travel.send.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

open class OrderSendList : BaseObservable() {

    /**
     * freight_id : 1526033814
     * freight : 10
     * goods_info : 其他:货物/重量：9 KG/体积：9 m³
     * from_address : 广西南宁青秀区
     * to_address : 广西南宁江南区
     * status : 1
     * status_name : 待接单
     * user : {"name":"张三","phone":"171159758414","avatar":"/upload/avatar/5b0242652550a.png","rank_text":"白银"}
     */

    @SerializedName("freight_id")
    @get:Bindable
    var freightId: String? = null
        set(freightId) {
            field = freightId
            notifyChange(BR.freightId)
        }
    @SerializedName("freight")
    @get:Bindable
    var freight: Double = 0.toDouble()
        set(freight) {
            field = freight
            notifyChange(BR.freight)
        }
    @SerializedName("goods_info")
    @get:Bindable
    var goodsInfo: String? = null
        set(goodsInfo) {
            field = goodsInfo
            notifyChange(BR.goodsInfo)
        }
    @SerializedName("from_address")
    @get:Bindable
    var fromAddress: String? = null
        set(fromAddress) {
            field = fromAddress
            notifyChange(BR.fromAddress)
        }
    @SerializedName("to_address")
    @get:Bindable
    var toAddress: String? = null
        set(toAddress) {
            field = toAddress
            notifyChange(BR.toAddress)
        }
    @SerializedName("status")
    @get:Bindable
    var status: Int = 0
        set(status) {
            field = status
            notifyChange(BR.status)
        }
    @SerializedName("status_name")
    @get:Bindable
    var statusName: String? = null
        set(statusName) {
            field = statusName
            notifyChange(BR.statusName)
        }
    @SerializedName("user")
    @get:Bindable
    var user: UserBean? = null
        set(user) {
            field = user
            notifyChange(BR.user)
        }
    @Transient
    private var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

    @Synchronized
    private fun notifyChange(propertyId: Int) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.notifyChange(this, propertyId)
    }

    @Synchronized
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.add(callback)

    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry!!.remove(callback)
        }
    }

    open class UserBean : Observable {
        /**
         * name : 张三
         * phone : 171159758414
         * avatar : /upload/avatar/5b0242652550a.png
         * rank_text : 白银
         */

        @SerializedName("name")
        @get:Bindable
        var name: String? = null
            set(name) {
                field = name
                notifyChange(BR.name)
            }
        @SerializedName("phone")
        @get:Bindable
        var phone: String? = null
            set(phone) {
                field = phone
                notifyChange(BR.phone)
            }
        @SerializedName("avatar")
        @get:Bindable
        var avatar: String? = null
            set(avatar) {
                field = avatar
                notifyChange(BR.avatar)
            }
        @SerializedName("rank_text")
        @get:Bindable
        var rankText: String? = null
            set(rankText) {
                field = rankText
                notifyChange(BR.rankText)
            }
        @Transient
        private var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

        @Synchronized
        private fun notifyChange(propertyId: Int) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = PropertyChangeRegistry()
            }
            propertyChangeRegistry!!.notifyChange(this, propertyId)
        }

        @Synchronized
        override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
            if (propertyChangeRegistry == null) {
                propertyChangeRegistry = PropertyChangeRegistry()
            }
            propertyChangeRegistry!!.add(callback)

        }

        @Synchronized
        override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
            if (propertyChangeRegistry != null) {
                propertyChangeRegistry!!.remove(callback)
            }
        }
    }
}