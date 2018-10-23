package shy.car.sdk.travel.message.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class MessageList:BaseObservable() {
    /**
     * id : 13
     * img :
     * title : 提现已经被拒绝
     * type : 2
     * typeText : 服务
     * content : 您的提现已经被拒绝，拒绝原因：订单有未完成
     * created_at : 2018.05.16 18:38:01
     */

    @SerializedName("id")
    @get:Bindable
    var id: String? = null
        set(id) {
            field = id
            notifyChange(BR.id)
        }
    @SerializedName("img")
    @get:Bindable
    var img: String? = null
        set(img) {
            field = img
            notifyChange(BR.img)
        }
    @SerializedName("title")
    @get:Bindable
    var title: String? = null
        set(title) {
            field = title
            notifyChange(BR.title)
        }
    @SerializedName("type")
    @get:Bindable
    var type: String? = null
        set(type) {
            field = type
            notifyChange(BR.type)
        }
    @SerializedName("typeText")
    @get:Bindable
    var typeText: String? = null
        set(typeText) {
            field = typeText
            notifyChange(BR.typeText)
        }
    @SerializedName("content")
    @get:Bindable
    var content: String? = null
        set(content) {
            field = content
            notifyChange(BR.content)
        }
    @SerializedName("created_at")
    @get:Bindable
    var createdAt: String? = null
        set(createdAt) {
            field = createdAt
            notifyChange(BR.createdAt)
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