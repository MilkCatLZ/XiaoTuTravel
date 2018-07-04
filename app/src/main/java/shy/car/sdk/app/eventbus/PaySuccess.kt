package shy.car.sdk.app.eventbus

import shy.car.sdk.travel.pay.data.PayMethod

class PaySuccess(var oid: String = "") {
    var payMethod: PayMethod? = null
    var price: String? = null
}