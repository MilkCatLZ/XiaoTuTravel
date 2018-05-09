package shy.car.sdk.travel.user.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.alibaba.fastjson.annotation.JSONField;
import shy.car.sdk.BR;


/**
 * Created by LZ on 2016/8/26.
 * 用户
 */
public class UserBase extends BaseObservable {
    
    public static final String UID = "uid";
    public static final String PHONE = "phone";
    //    public static final String BIND_WEIXIN = "bind_weixin";
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
    
    @JSONField(name = UID)
    private int uid;
    @JSONField(name = PHONE)
    private String phone;
    //    @JSONField(name = BIND_WEIXIN)
//    private int bindWeixin;
    @JSONField(name = ACCESS_TOKEN)
    private String access_token;
    @JSONField(name = NICKNAME)
    private String nickName;
    @JSONField(name = AVATAR)
    private String avatar;
    @JSONField(name = EXPIRES_IN)
    private long expiresIn;
    /**
     * 1空闲 2繁忙
     */
    @JSONField(name = STATUS)
    private int status;
    /**
     * 1平台配送员 2店铺配送员
     */
    @JSONField(name = BUYER_TYPE)
    private int type;
    /**
     * 密码：1:已设置，0：未设置密码
     */
    @JSONField(name = PASSWORD)
    private int password;
    /**
     * 默认配送点名称
     */
    @JSONField(name = "delivery_name")
    private String deliveryName;
    /**
     * 支付密码 1 已设置 0 未设置
     */
    @JSONField(name = "payment_password")
    private int paymentPassword;
    /**
     * 推送设置 1 接收 2 不接收
     */
    @JSONField(name = "msg_remind")
    private int msgRemind;
    
    /**
     * 直属上级
     */
    @JSONField(name = "superior_name")
    private String superiorName;
    /**
     * 直属上级电话
     */
    @JSONField(name = "superior_phone")
    private String superiorPhone;
    /**
     * 直属上级级别
     */
    @JSONField(name = "superior_level")
    private int superiorLevel;
    /**
     * 个人级别
     */
    @JSONField(name = "job_level")
    private int jobLevel;
    
    /**
     * 工作时间
     */
    @JSONField(name = "work_time")
    private WorkTime workTime;
    /**
     * 是否为合伙人 1 是 0 非
     */
    @JSONField(name = "partner")
    private int partner;
    
    @Bindable
    public int getPartner() {
        return partner;
    }
    
    public void setPartner(int partner) {
        this.partner = partner;
        notifyPropertyChanged(BR.partner);
    }
    
    @Bindable
    public WorkTime getWorkTime() {
        return workTime;
    }
    
    public void setWorkTime(WorkTime workTime) {
        this.workTime = workTime;
        notifyPropertyChanged(BR.workTime);
    }
    
    public static class WorkTime extends BaseObservable {
        @JSONField(name = "start_time")
        long startTime;
        @JSONField(name = "end_time")
        long endTime;
        
        @Bindable
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
            notifyPropertyChanged(BR.startTime);
        }
        
        @Bindable
        public long getEndTime() {
            return endTime;
        }
        
        public void setEndTime(long endTime) {
            this.endTime = endTime;
            notifyPropertyChanged(BR.endTime);
        }
    }
    
    
    @Bindable
    public int getJobLevel() {
        return jobLevel;
    }
    
    public void setJobLevel(int jobLevel) {
        this.jobLevel = jobLevel;
        notifyPropertyChanged(BR.jobLevel);
    }
    
    @Bindable
    public String getSuperiorName() {
        return superiorName;
    }
    
    public void setSuperiorName(String superiorName) {
        this.superiorName = superiorName;
        notifyPropertyChanged(BR.superiorName);
    }
    
    @Bindable
    public String getSuperiorPhone() {
        return superiorPhone;
        
    }
    
    public void setSuperiorPhone(String superiorPhone) {
        this.superiorPhone = superiorPhone;
        notifyPropertyChanged(BR.superiorPhone);
    }
    
    @Bindable
    public int getSuperiorLevel() {
        return superiorLevel;
    }
    
    public void setSuperiorLevel(int superiorLevel) {
        this.superiorLevel = superiorLevel;
        notifyPropertyChanged(BR.superiorLevel);
    }
    
    @Bindable
    public int getMsgRemind() {
        return msgRemind;
    }
    
    public void setMsgRemind(int msgRemind) {
        this.msgRemind = msgRemind;
        notifyPropertyChanged(BR.msgRemind);
    }
    
    @Bindable
    public String getDeliveryName() {
        return deliveryName;
    }
    
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
        notifyPropertyChanged(BR.deliveryName);
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
    
    
    @Bindable
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
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
    public int getUid() { return uid;}
    
    public void setUid(int uid) {
        this.uid = uid;
        notifyPropertyChanged(BR.uid);
    }
    
    @Bindable
    public String getPhone() { return phone;}
    
    public void setPhone(String phone) {
        if ("0".equals(phone)) {
            this.phone = "";
        } else {
            this.phone = phone;
        }
        notifyPropertyChanged(BR.phone);
    }

//    public int getBindWeixin() { return bindWeixin;}
//
//    public void setBindWeixin(int bindWeixin) { this.bindWeixin = bindWeixin;}
    
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
    
    @JSONField(serialize = false)
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
