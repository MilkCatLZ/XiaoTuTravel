package shy.car.sdk.travel.user.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import shy.car.sdk.BR;


/**
 * Created by LZ on 2016/8/26.
 * 用户
 */
public class UserBase extends BaseObservable {
    public static class PromissState {
        public static final int MONEY_PAYED = 1;
        public static final int MONEY_NOT_PAYED = 2;
    }


    public static final String UID = "uid";
    public static final String PHONE = "phone";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String AVATAR = "avatar";
    public static final String NICKNAME = "name";
    public static final String EXPIRES_IN = "expires_in";
    public static final String BUYER_TYPE = "type";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";

    /**
     * uid : 10314
     * phone : 18577845220
     * bind_weixin : 0
     */

    @SerializedName( UID)
    private int uid;
    @SerializedName( PHONE)
    private String phone;
    //    @SerializedName( BIND_WEIXIN)
//    private int bindWeixin;
    @SerializedName( ACCESS_TOKEN)
    private String access_token;
    @SerializedName( NICKNAME)
    private String nickName;
    @SerializedName( AVATAR)
    private String avatar;
    @SerializedName( EXPIRES_IN)
    private long expiresIn;
    /**
     * 1:已认证。2：未认证
     */
    @SerializedName( STATUS)
    private int status;

    /**
     * 密码：1:已设置，0：未设置密码
     */
    @SerializedName( PASSWORD)
    private int password;

    /**
     * 支付密码 1 已设置 0 未设置
     */
    @SerializedName( "payment_password")
    private int paymentPassword;
    /**
     * 推送设置 1 接收 2 不接收
     */
    @SerializedName( "msg_remind")
    private int msgRemind;

    /**
     * 保证金缴纳.>0已缴纳，<=0未缴纳
     */
    @SerializedName( "promise_money")
    private double promiseMoney;

    /**
     * 用户实名认证
     * .1=已认证，0=未认证
     */
    @SerializedName( "user_verify")
    private int userVerify;

    public boolean isUserVerify() {
        return getUserVerify() == 1;
    }

    @Bindable
    public int getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(int userVerify) {
        this.userVerify = userVerify;
        notifyPropertyChanged(BR.userVerify);
    }

    /**
     * 保证金缴纳：1：已缴纳，2：未缴纳
     */
    @Bindable
    public boolean getPromiseMoneyState() {
        return promiseMoney > 0;
    }


    @Bindable
    public double getPromiseMoney() {
        return promiseMoney;
    }

    public void setPromiseMoney(double promiseMoney) {
        this.promiseMoney = promiseMoney;
        notifyPropertyChanged(BR.promiseMoney);
    }

    @Bindable
    public int getMsgRemind() {
        return msgRemind;
    }

    public void setMsgRemind(int msgRemind) {
        this.msgRemind = msgRemind;
        notifyPropertyChanged(BR.msgRemind);
    }

    public int getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(int paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    private int loginType;

    private long loginTime;

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    @Bindable
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
        notifyPropertyChanged(BR.uid);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if ("0".equals(phone)) {
            this.phone = "";
        } else {
            this.phone = phone;
        }
        notifyPropertyChanged(BR.phone);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
        notifyPropertyChanged(BR.login);
    }

    @Bindable
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        notifyPropertyChanged(BR.nickName);
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    @Bindable
    public boolean isLogin() {
        return com.base.util.StringUtils.isNotEmpty(access_token);
    }

    @Override
    public String toString() {
        return "UserBase{" +
                "uid=" + uid +
                ", phone='" + phone + '\'' +
//               ", bindWeixin=" + bindWeixin +
                ", access_token='" + access_token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", expiresIn=" + expiresIn +
                ", loginType=" + loginType +
                ", loginTime=" + loginTime +
                '}';
    }
}
