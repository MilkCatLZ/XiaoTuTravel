package mall.lianni.alipay;

import com.google.gson.annotations.SerializedName;

public class PayModel {


    /**
     * payment : 2
     * appid : 微信支付APPID
     * partnerid : 微信支付商户号
     * prepay_id : 预支付交易会话标识
     * package : Sign=WXPay
     * noncestr : 随机字符串
     * timestamp : 时间戳
     * price : 100000
     * sign : 签名
     */

    @SerializedName("payment")
    private String payment;
    @SerializedName("appid")
    private String appid;
    @SerializedName("partnerid")
    private String partnerid;
    @SerializedName("prepay_id")
    private String prepayId;
    @SerializedName("package")
    private String packageX;
    @SerializedName("noncestr")
    private String noncestr;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("price")
    private double price;
    @SerializedName("sign")
    private String sign;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
