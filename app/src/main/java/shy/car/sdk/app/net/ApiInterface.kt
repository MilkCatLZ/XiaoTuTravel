package shy.car.sdk.app.net

import io.reactivex.Observable
import retrofit2.http.*
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail
import shy.car.sdk.travel.rent.data.NearCarList
import shy.car.sdk.travel.send.data.OrderSendDetail
import shy.car.sdk.travel.send.data.OrderSendList
import shy.car.sdk.travel.take.data.TakeOrderDetail
import shy.car.sdk.travel.take.data.TakeOrderList


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
    @FormUrlEncoded
    @POST("phone/{phone}/captcha")
    fun gerVerify(@Path(ParamsConstant.Phone) phone: String, @Field("") params: String=""): Observable<String>

    /**
     * 登录
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @GET("oauth/access_token")
    fun login(@Query(ParamsConstant.UserName) userName: String, @Query(ParamsConstant.PASSWORD) verify: String, @Query(ParamsConstant.PASSWORD) grant_type: String = "password", @Query(ParamsConstant.ClientID) client_id: String = "10001"): Observable<String>

    /**
     * 修改密码
     * JsonString：{"phone":"","password":""}
     */
    @FormUrlEncoded
    @PUT("xt/password")
    fun setupPassWord(@Query(ParamsConstant.UID) uid: String, @Query(ParamsConstant.PASSWORD) passWord: String): Observable<String>

    /**
     * 获取城市列表
     * JsonString：{"phone":"","password":""}
     */
    @GET("xt/city")
    fun getCityList(): Observable<ArrayList<CurrentLocation>>

    /**
     * 获取接单列表
     *
     */
    @GET("xt/takeOrderList")
    fun getTakeOrderList(@Query(ParamsConstant.PageIndex) pageIndex: Int,
                         @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<TakeOrderList>>

    /**
     * 获取单个接单详情
     *
     */
    @GET("xt/takeOrderDetail")
    fun getTakeOrderDetail(@Query(ParamsConstant.OrderId) oid: String): Observable<TakeOrderDetail>

    /**
     * 获取单个接单详情
     *
     */
    @GET("xt/sendOrderDetail")
    fun getSendOrderDetail(@Query(ParamsConstant.OrderId) oid: String): Observable<OrderSendDetail>

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

    /**
     * 保证金记录
     */
    @GET("xt/promise_detail_list")
    fun getPromiseMoneyDetailList(): Observable<ArrayList<PromiseMoneyDetail>>
}