package shy.car.sdk.travel.take.data

open class OrderState {
    companion object {
        const val Close = 0
        const val StateWaitTake = 1
        const val StateWaitPay = 10
        const val StateSending = 20
        const val StateSended = 30
        const val StateFinish = 40
    }
}