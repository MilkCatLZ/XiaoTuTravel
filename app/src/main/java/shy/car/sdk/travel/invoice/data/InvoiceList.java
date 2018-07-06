package shy.car.sdk.travel.invoice.data;


import android.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

import shy.car.sdk.BuildConfig;


public class InvoiceList extends BaseObservable {

    int id;

    private List<Orders> orders=new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Orders> getOrders() {
        if (BuildConfig.DEBUG&&orders.isEmpty()) {
            for (int i = 0; i < 2; i++) {
                Orders order = new Orders();
                order.id = i;
                orders.add(order);
            }
        }
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public class Orders {
        int id;
    }
}
