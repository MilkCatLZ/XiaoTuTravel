package shy.car.sdk.app.net

import io.reactivex.Observable
import retrofit2.http.*
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.data.CarSelectInfo
import shy.car.sdk.travel.send.data.OrderSendList
import shy.car.sdk.travel.take.data.OrderList
import shy.car.sdk.travel.rent.data.NearCarList


/**
 * 统一接口定义
 * sample：
 *
 *      @GET("TomCatDemo/JsonDemo")
 *      fun getTests(@Query("uid") uid: String, @Query("sex") sex: String): Observable<ArrayList<Tests>>
 *
 *      @FormUrlEncoded
 *      @POST("TomCatDemo/JsonDemo")
 *      fun postTests(@Field("uid") uid: String, @Field("sex") sex: String): Observable<ArrayList<Tests>>
 */
interface ApiInterface {
    /**
     * 获取验证码
     */
    @GET("phone/captcha")
    fun gerVerify(@Query(ParamsConstant.Phone) params: String): Observable<String>

    /**
     * 登录
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @FormUrlEncoded
    @POST("xt/login")
    fun login(@Field("") params: String): Observable<String>

    /**
     * 修改密码
     * JsonString：{"phone":"","password":""}
     */
    @FormUrlEncoded
    @PUT("xt/password")
    fun setupPassWord(@Field("") params: String): Observable<String>

    /**
     * 获取城市列表
     * JsonString：{"phone":"","password":""}
     */
    @GET("xt/city")
    fun getCityList(): Observable<ArrayList<City>>

    /**
     * 获取接单列表
     *
     */
    @GET("xt/takeOrderList")
    fun getTakeOrderList(@Query(ParamsConstant.PageIndex) pageIndex: Int,
                         @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<OrderList>>

    /**
     * 获取发货列表
     *
     */
    @GET("xt/orderSendList")
    fun getOrderSendList(@Query(ParamsConstant.PageIndex) pageIndex: Int,
                         @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<OrderSendList>>

    /**
     * 获取发货列表
     *
     */
    @GET("xt/orderMineList")
    fun getOrdreMineList(@Query(ParamsConstant.PageIndex) pageIndex: Int,
                         @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<OrderMineList>>

    /**
     * 检查更新
     *
     */
    @GET("xt/update")
    fun getUpdateInfo(): Observable<String>

    /**
     * 获取发货列表
     *
     */
    @GET("xt/message")
    fun getMessageList(@Query(ParamsConstant.PageIndex) pageIndex: Int,
                       @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<MessageList>>

    /**
     * 附近车辆列表
     */
    @GET("xt/near_List")
    fun getNearList(@Query(ParamsConstant.Lat) lat: String,
                    @Query(ParamsConstant.Lng) lng: String,
                    @Query(ParamsConstant.KeyWord) keyWord: String,
                    @Query(ParamsConstant.PageIndex) pageIndex: Int,
                    @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<NearCarList>>

    /**
     * 附近车辆列表
     */
    @GET("xt/car_list")
    fun getCarList(): Observable<ArrayList<CarSelectInfo>>
}