package shy.car.sdk.app.eventbus

import org.greenrobot.eventbus.EventBus

class RefreshOrderList {
    companion object {
        fun refreshOrderList() {
            EventBus.getDefault()
                    .post(RefreshOrderList())
        }
    }
}