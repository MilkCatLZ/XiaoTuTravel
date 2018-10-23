package shy.car.sdk.travel.setting.data;


import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;


public class ShareDetail extends BaseObservable {

    /**
     * name : 小兔出行云
     * title : 您的好友邀请你下载小兔出行云APP，拿大礼
     * content : 你身边最便捷的出行方式
     * img : /assets/img/pay/wx.png
     * urls : {"android":"http://www.baidu.com","ios":"http://www.baidu.com"}
     */

    @SerializedName("name")
    private String name;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("img")
    private String img;
    @SerializedName("code")
    private String code;
    @SerializedName("coupon_sum")
    private String couponSum;
    @SerializedName("urls")
    private UrlsBean urls;


    public String getCouponText() {
        return couponSum + "元优惠券一张";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCouponSum() {
        return couponSum;
    }

    public void setCouponSum(String couponSum) {
        this.couponSum = couponSum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public UrlsBean getUrls() {
        return urls;
    }

    public void setUrls(UrlsBean urls) {
        this.urls = urls;
    }

    public static class UrlsBean {
        /**
         * android : http://www.baidu.com
         * ios : http://www.baidu.com
         */

        @SerializedName("android")
        private String android;
        @SerializedName("ios")
        private String ios;

        public String getAndroid() {
            return android;
        }

        public void setAndroid(String android) {
            this.android = android;
        }

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }
    }
}
