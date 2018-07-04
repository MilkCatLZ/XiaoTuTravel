package shy.car.sdk.travel.rent.ui

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.RentOrderDetail


/**
 * create by LZ at 2018/07/04
 * 租车评价
 */
@Route(path = RouteMap.RentComment)
class RentOrderCommentActivity :XTBaseActivity(){

    @Autowired(name = Object1)
    @JvmField
    var detail: RentOrderDetail? = null
}