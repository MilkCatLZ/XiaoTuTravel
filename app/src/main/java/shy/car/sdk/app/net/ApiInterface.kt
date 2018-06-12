package shy.car.sdk.app.net

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
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
import shy.car.sdk.travel.user.data.UserDetailCache
import java.io.File


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
    fun gerVerify(@Path(ParamsConstant.Phone) phone: String, @Field("") name: String = ""): Observable<JsonObject>


    /**
     * 登录
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @GET("oauth/access_token")
    fun login(@Query(ParamsConstant.UserName) userName: String, @Query(ParamsConstant.PASSWORD) verify: String, @Query(ParamsConstant.GrantType) grant_type: String = "password", @Query(ParamsConstant.ClientID) client_id: String = "10001"): Observable<JsonObject>

    /**
     * 登录
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @DELETE("oauth/access_token")
    fun logout(@Query("") params: String = ""): Observable<JsonObject>

    /**
     * 获取用户详情
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @GET("users/info")
    fun gerUserDetail(): Observable<UserDetailCache>

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

    /**
     * 保证金记录
     */
    @FormUrlEncoded
    @POST("avatar")
    fun uploadAvatar(@Field("img") image: File): Observable<JsonObject>

    /**
     * 提交身份认证
     */
    @Multipart
    @Headers("Content-Type: text/plain")
    @POST("users/identity")
    fun uploadUserVerify(@Part imageList: List<MultipartBody.Part>): Observable<JsonObject>
//    /**
//     * 提交身份认证
//     */
//    @Multipart
//    @Headers("Content-Type: text/plain")
//    @POST("users/identity")
//    fun uploadUserVerify(@PartMap map: Map<String, String>, @Part imageList: List<MultipartBody.Part>): Observable<JsonObject>
//    /**
//     * 提交身份认证
//     */
//    @Multipart
//    @POST("users/identity")
//    fun uploadUserVerify(@Part(ParamsConstant.Name) name:String,@Part(ParamsConstant.IDcard) idNum:String,@Part imageList:List<MultipartBody.Part>): Observable<JsonObject>

//    @Multipart
//    @POST("users/identity")
//    fun uploadUserVerify(@Part(ParamsConstant.Name) name: String, @Part(ParamsConstant.IDcard) idNum: String, @Part holdImage: MultipartBody.Part, @Part frontImage: MultipartBody.Part, @Part driveImage: MultipartBody.Part): Observable<JsonObject>


}