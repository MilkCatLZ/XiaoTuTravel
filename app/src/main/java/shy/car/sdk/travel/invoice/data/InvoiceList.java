package shy.car.sdk.travel.invoice.data;


import android.databinding.BaseObservable;

import java.util.List;


public class InvoiceList extends BaseObservable {
    
    int id;
    
    private List<Orders> orders;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<Orders> getOrders() {
        return orders;
    }
    
    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
    
    public class Orders {
        int id;
    }
}
