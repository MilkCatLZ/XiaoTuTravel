package shy.car.sdk.travel.interfaces

import shy.car.sdk.travel.location.data.City

interface MapLocationRefreshListener {
    fun onLocationChange(city: City)
}