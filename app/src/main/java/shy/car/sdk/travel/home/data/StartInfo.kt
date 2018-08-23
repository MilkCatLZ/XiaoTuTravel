package shy.car.sdk.travel.home.data

import com.google.gson.annotations.SerializedName

class StartInfo {
    fun needAD(): Boolean {
        if (ad != null && !ad?.img.isNullOrEmpty()) {
            return true
        }
        return false
    }

    /**
     * start : {"id":1,"title":"启动也","img":"http://img.abc.com/ad/5b712b6adfe2c.jpg","url":""}
     * ad : {"id":4,"title":"呜呜呜","img":"http://img.abc.com/ad/5b75338842ce9.jpg","url":""}
     */

    @SerializedName("start")
    var start: StartBean? = null
    @SerializedName("ad")
    var ad: AdBean? = null

    open class StartBean {
        /**
         * id : 1
         * title : 启动也
         * img : http://img.abc.com/ad/5b712b6adfe2c.jpg
         * url :
         */

        @SerializedName("id")
        var id: String? = null
        @SerializedName("title")
        var title: String? = null
        @SerializedName("img")
        var img: String? = null
        @SerializedName("url")
        var url: String? = null
    }

    open class AdBean {
        /**
         * id : 4
         * title : 呜呜呜
         * img : http://img.abc.com/ad/5b75338842ce9.jpg
         * url :
         */

        @SerializedName("id")
        var id: String? = null
        @SerializedName("title")
        var title: String? = null
        @SerializedName("img")
        var img: String? = null
        @SerializedName("url")
        var url: String? = null
    }
}