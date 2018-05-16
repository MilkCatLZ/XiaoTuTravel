package shy.car.sdk.app.net

import io.reactivex.Observable
import retrofit2.http.*
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.order.mine.data.OrderMineList
import shy.car.sdk.travel.order.send.data.OrderSendList
import shy.car.sdk.travel.order.take.data.OrderList


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
    fun getCityList(): Observable<java.util.ArrayList<City>>

    /**
     * 获取接单列表
     *
     */
    @GET("xt/takeOrderList")
    fun getTakeOrderList(@Query(ParamsConstant.PageIndex) pageIndex: Int, @Query(ParamsConstant.PageSize) pageSize: Int): Observable<java.util.ArrayList<OrderList>>

    /**
     * 获取发货列表
     *
     */
    @GET("xt/orderSendList")
    fun getOrderSendList(@Query(ParamsConstant.PageIndex) pageIndex: Int, @Query(ParamsConstant.PageSize) pageSize: Int): Observable<java.util.ArrayList<OrderSendList>>

    /**
     * 获取发货列表
     *
     */
    @GET("xt/orderMineList")
    fun getOrdreMineList(@Query(ParamsConstant.PageIndex) pageIndex: Int, @Query(ParamsConstant.PageSize) pageSize: Int): Observable<java.util.ArrayList<OrderMineList>>

    /**
     * 检查更新
     *
     */
    @GET("xt/update")
    fun getUpdateInfo(): Observable<String>
}