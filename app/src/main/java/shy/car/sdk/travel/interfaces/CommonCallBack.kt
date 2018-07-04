package shy.car.sdk.travel.interfaces

interface CommonCallBack<T> {
    fun onSuccess(t: T)
    fun onError(e: Throwable)
}