package shy.car.sdk.travel.invoice.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.google.gson.annotations.SerializedName;

import shy.car.sdk.BR;


public class InvoiceHistory extends BaseObservable implements Observable {
    
    /**
     * id : 1003
     * title : 广西科技有限公司
     * money : 1
     * status : 1
     * status_text : 申请中
     * created_at : 2018.07.04 11:29:56
     */
    
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("money")
    private Double money;
    @SerializedName("status")
    private int status;
    @SerializedName("status_text")
    private String statusText;
    @SerializedName("created_at")
    private String createdAt;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    
    @Bindable
    public int getId() { return id;}
    
    public void setId(int id) {
        this.id = id;
        notifyChange(BR.id);
    }
    
    @Bindable
    public String getTitle() { return title;}
    
    public void setTitle(String title) {
        this.title = title;
        notifyChange(BR.title);
    }
    
    @Bindable
    public Double getMoney() { return money;}
    
    public void setMoney(Double money) {
        this.money = money;
        notifyChange(BR.money);
    }
    
    @Bindable
    public int getStatus() { return status;}
    
    public void setStatus(int status) {
        this.status = status;
        notifyChange(BR.status);
    }
    
    @Bindable
    public String getStatusText() { return statusText;}
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
        notifyChange(BR.statusText);
    }
    
    @Bindable
    public String getCreatedAt() { return createdAt;}
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        notifyChange(BR.createdAt);
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
