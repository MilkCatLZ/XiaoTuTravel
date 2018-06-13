package shy.car.sdk.travel.rent.data;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import shy.car.sdk.BR;


public class Ca extends BaseObservable implements Observable {
    
    
    /**
     * car_id : 1
     * car_model : 奇瑞EQ1
     * car_model_img : http://static.car.com/car/2018/05/10/Aosajfo12dd.jpg
     * plate_number : 桂A88888
     * color : 白色
     * surplus_mileage : 135
     * address : 广西南宁市西乡塘区相思湖西路
     * lng : 108.248593
     * lat : 22.921449
     * cost : {"minute":0.2,"kilometre":0.88,"low_price":"最低消费8元"}
     * discounts : [{"id":24,"txt":"24小时整日租","price":"88"},{"id":"夜","txt":"20:00-次日8:00","price":"88"}]
     */
    
    @SerializedName("car_id")
    private String carId;
    @SerializedName("car_model")
    private String carModel;
    @SerializedName("car_model_img")
    private String carModelImg;
    @SerializedName("plate_number")
    private String plateNumber;
    @SerializedName("color")
    private String color;
    @SerializedName("surplus_mileage")
    private double surplusMileage;
    @SerializedName("address")
    private String address;
    @SerializedName("lng")
    private double lng;
    @SerializedName("lat")
    private double lat;
    @SerializedName("cost")
    private Cost cost;
    @SerializedName("discounts")
    private List<Discounts> discounts;
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    
    @Bindable
    public String getCarId() { return carId;}
    
    public void setCarId(String carId) {
        this.carId = carId;
        notifyChange(BR.carId);
    }
    
    @Bindable
    public String getCarModel() { return carModel;}
    
    public void setCarModel(String carModel) {
        this.carModel = carModel;
        notifyChange(BR.carModel);
    }
    
    @Bindable
    public String getCarModelImg() { return carModelImg;}
    
    public void setCarModelImg(String carModelImg) {
        this.carModelImg = carModelImg;
        notifyChange(BR.carModelImg);
    }
    
    @Bindable
    public String getPlateNumber() { return plateNumber;}
    
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        notifyChange(BR.plateNumber);
    }
    
    @Bindable
    public String getColor() { return color;}
    
    public void setColor(String color) {
        this.color = color;
        notifyChange(BR.color);
    }
    
    @Bindable
    public double getSurplusMileage() { return surplusMileage;}
    
    public void setSurplusMileage(double surplusMileage) {
        this.surplusMileage = surplusMileage;
        notifyChange(BR.surplusMileage);
    }
    
    @Bindable
    public String getAddress() { return address;}
    
    public void setAddress(String address) {
        this.address = address;
        notifyChange(BR.address);
    }
    
    @Bindable
    public double getLng() { return lng;}
    
    public void setLng(double lng) {
        this.lng = lng;
        notifyChange(BR.lng);
    }
    
    @Bindable
    public double getLat() { return lat;}
    
    public void setLat(double lat) {
        this.lat = lat;
        notifyChange(BR.lat);
    }
    
    @Bindable
    public Cost getCost() { return cost;}
    
    public void setCost(Cost cost) {
        this.cost = cost;
        notifyChange(BR.cost);
    }
    
    @Bindable
    public List<Discounts> getDiscounts() { return discounts;}
    
    public void setDiscounts(List<Discounts> discounts) {
        this.discounts = discounts;
        notifyChange(BR.discounts);
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
    
    public static class Cost implements Observable {
        /**
         * minute : 0.2
         * kilometre : 0.88
         * low_price : 最低消费8元
         */
        
        @SerializedName("minute")
        private double minute;
        @SerializedName("kilometre")
        private double kilometre;
        @SerializedName("low_price")
        private String lowPrice;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
        
        @Bindable
        public double getMinute() { return minute;}
        
        public void setMinute(double minute) {
            this.minute = minute;
            notifyChange(BR.minute);
        }
        
        @Bindable
        public double getKilometre() { return kilometre;}
        
        public void setKilometre(double kilometre) {
            this.kilometre = kilometre;
            notifyChange(BR.kilometre);
        }
        
        @Bindable
        public String getLowPrice() { return lowPrice;}
        
        public void setLowPrice(String lowPrice) {
            this.lowPrice = lowPrice;
            notifyChange(BR.lowPrice);
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
    
    
    public static class Discounts implements Observable {
        /**
         * id : 24
         * txt : 24小时整日租
         * price : 88
         */
        
        @SerializedName("id")
        private String id;
        @SerializedName("txt")
        private String txt;
        @SerializedName("price")
        private String price;
        private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    
        @Bindable
        public String getId() { return id;}
        
        public void setId(String id) {
            this.id = id;
            notifyChange(BR.id);
        }
        
        @Bindable
        public String getTxt() { return txt;}
        
        public void setTxt(String txt) {
            this.txt = txt;
            notifyChange(BR.txt);
        }
        
        @Bindable
        public String getPrice() { return price;}
        
        public void setPrice(String price) {
            this.price = price;
            notifyChange(BR.price);
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
