package shy.car.sdk.travel.invoice.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import shy.car.sdk.BR;
import shy.car.sdk.BuildConfig;


public class InvoiceList extends BaseObservable implements Observable {
    
    
    /**
     * month : 5
     * list : [{"car_plate_number":"桂E88888","car_model_name":"A4","money":0,"created_at":"2018.05.11 18:15:41"},{"car_plate_number":"桂E88888","car_model_name":"A4","money":10,"created_at":"2018.05.11 18:16:54"}]
     */
    
    @SerializedName("month")
    private int month;
    @SerializedName("list")
    private List<Orders> orders;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    
    public String getMonthText() {
        return month + "月";
    }
    
    @Bindable
    public int getMonth() { return month;}
    
    public void setMonth(int month) {
        this.month = month;
        notifyChange(BR.month);
    }
    
    @Bindable
    public List<Orders> getOrders() {
        if ((orders == null || orders.isEmpty()) && BuildConfig.DEBUG) {
            orders = new ArrayList<>();
            for(int i = 0; i < 3; i++) {
                Orders order =new Orders();
                order.id= String.valueOf(i);
                order.carModelName="EQ1";
                order.carPlateNumber="桂A77995";
                order.money=554.23;
                order.createdAt="2018.07.07 10:38";
                orders.add(order);
            }
        }
        return orders;
    }
    
    public void setOrders(List<Orders> list) {
        this.orders = list;
        notifyChange(BR.orders);
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
    
    public static class Orders implements Observable {
        /**
         * car_plate_number : 桂E88888
         * car_model_name : A4
         * money : 0
         * created_at : 2018.05.11 18:15:41
         */
        @Bindable
        public String getCarInfo() {
            return carModelName + " | " + carPlateNumber;
        }
        
        
        @SerializedName("id")
        private String id;
        @SerializedName("car_plate_number")
        private String carPlateNumber;
        @SerializedName("car_model_name")
        private String carModelName;
        @SerializedName("money")
        private Double money;
        @SerializedName("created_at")
        private String createdAt;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
        
        @Bindable
        public String getId() {
            if (BuildConfig.DEBUG) {
                return String.valueOf(Math.random() * 100);
            }
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
            notifyChange(BR.id);
        }
        
        public PropertyChangeRegistry getPropertyChangeRegistry() {
            return propertyChangeRegistry;
        }
        
        public void setPropertyChangeRegistry(PropertyChangeRegistry propertyChangeRegistry) {
            this.propertyChangeRegistry = propertyChangeRegistry;
        }
        
        @Bindable
        public String getCarPlateNumber() { return carPlateNumber;}
        
        public void setCarPlateNumber(String carPlateNumber) {
            this.carPlateNumber = carPlateNumber;
            notifyChange(BR.carPlateNumber);
        }
        
        @Bindable
        public String getCarModelName() { return carModelName;}
        
        public void setCarModelName(String carModelName) {
            this.carModelName = carModelName;
            notifyChange(BR.carModelName);
        }
        
        @Bindable
        public Double getMoney() { return money;}
        
        public void setMoney(Double money) {
            this.money = money;
            notifyChange(BR.money);
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
}
