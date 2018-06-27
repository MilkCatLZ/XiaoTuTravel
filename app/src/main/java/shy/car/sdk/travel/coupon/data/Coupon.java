package shy.car.sdk.travel.coupon.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.google.gson.annotations.SerializedName;

import shy.car.sdk.BR;


public class Coupon extends BaseObservable implements Observable {

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
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyChange(BR.id);
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyChange(BR.type);
    }

    @Bindable
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        notifyChange(BR.typeName);
    }

    @Bindable
    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
        notifyChange(BR.conditions);
    }

    @Bindable
    public String getTakeEffectDate() {
        return takeEffectDate;
    }

    public void setTakeEffectDate(String takeEffectDate) {
        this.takeEffectDate = takeEffectDate;
        notifyChange(BR.takeEffectDate);
    }

    @Bindable
    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
        notifyChange(BR.invalidDate);
    }

    @Bindable
    public double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(double couponMoney) {
        this.couponMoney = couponMoney;
        notifyChange(BR.couponMoney);
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyChange(BR.status);
    }

    @Bindable
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
        notifyChange(BR.statusName);
    }

    private synchronized void notifyChange(int propertyId) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.notifyChange(this, propertyId);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.add(callback);

    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry.remove(callback);
        }
    }
}
