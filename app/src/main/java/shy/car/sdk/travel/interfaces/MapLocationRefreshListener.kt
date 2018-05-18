package shy.car.sdk.travel.interfaces

import shy.car.sdk.travel.location.data.City

interface MapLocationRefreshListener {
    abstract fun onLocationChange(city: City)

}