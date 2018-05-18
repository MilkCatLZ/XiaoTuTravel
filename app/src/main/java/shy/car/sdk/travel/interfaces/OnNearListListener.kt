package shy.car.sdk.travel.interfaces

import shy.car.sdk.travel.rent.data.NearCarList

interface OnNearListListener {
    fun getNearCarListSuccess(list: ArrayList<NearCarList>)
}