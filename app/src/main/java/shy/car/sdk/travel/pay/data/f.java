package shy.car.sdk.travel.pay.data;


import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;


public class f extends BaseObservable {
    
    /**
     * amount : 5
     * price : 5
     */
    
    @SerializedName("amount")
    private int amount;
    @SerializedName("price")
    private int price;
    
    public int getAmount() { return amount;}
    
    public void setAmount(int amount) { this.amount = amount;}
    
    public int getPrice() { return price;}
    
    public void setPrice(int price) { this.price = price;}
}
