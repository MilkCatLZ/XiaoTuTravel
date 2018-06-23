package shy.car.sdk.travel.coupon.data;


import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;


public class Coupon extends BaseObservable {
    
    /**
     * id : 2
     * type : 1
     * type_name : 现金券
     * conditions : 满50元可使用抵租金
     * take_effect_date : 2018.04.24 00:00:00
     * invalid_date : 2018.05.28 00:00:00
     * coupon_money : 5
     * status : 2
     * status_name : 未使用
     */
    
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private int type;
    @SerializedName("type_name")
    private String typeName;
    @SerializedName("conditions")
    private String conditions;
    @SerializedName("take_effect_date")
    private String takeEffectDate;
    @SerializedName("invalid_date")
    private String invalidDate;
    @SerializedName("coupon_money")
    private double couponMoney;
    @SerializedName("status")
    private int status;
    @SerializedName("status_name")
    private String statusName;
    
    public int getId() { return id;}
    
    public void setId(int id) { this.id = id;}
    
    public int getType() { return type;}
    
    public void setType(int type) { this.type = type;}
    
    public String getTypeName() { return typeName;}
    
    public void setTypeName(String typeName) { this.typeName = typeName;}
    
    public String getConditions() { return conditions;}
    
    public void setConditions(String conditions) { this.conditions = conditions;}
    
    public String getTakeEffectDate() { return takeEffectDate;}
    
    public void setTakeEffectDate(String takeEffectDate) { this.takeEffectDate = takeEffectDate;}
    
    public String getInvalidDate() { return invalidDate;}
    
    public void setInvalidDate(String invalidDate) { this.invalidDate = invalidDate;}
    
    public double getCouponMoney() { return couponMoney;}
    
    public void setCouponMoney(double couponMoney) { this.couponMoney = couponMoney;}
    
    public int getStatus() { return status;}
    
    public void setStatus(int status) { this.status = status;}
    
    public String getStatusName() { return statusName;}
    
    public void setStatusName(String statusName) { this.statusName = statusName;}
}
