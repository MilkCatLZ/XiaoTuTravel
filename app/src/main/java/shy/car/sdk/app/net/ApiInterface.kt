package shy.car.sdk.app.net

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.bank.data.BankType
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.pay.data.PayAmount
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail
import shy.car.sdk.travel.remain.data.RemainList
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.data.RentOrderDetail
import shy.car.sdk.travel.send.data.OrderSendDetail
import shy.car.sdk.travel.send.data.OrderSendList
import shy.car.sdk.travel.take.data.TakeOrderDetail
import shy.car.sdk.travel.take.data.TakeOrderList
import shy.car.sdk.travel.user.data.UserDetailCache


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
    fun gerVerify(@Path(ParamsConstant.Phone) phone: String, @Field("") name: String? = null): Observable<JsonObject>


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
    fun logout(@Query("") params: String? = null): Observable<JsonObject>

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
    @GET("cities")
    fun getCityList(@Query(ParamsConstant.Offset) offset: Int = 0, @Query(ParamsConstant.Limit) limit: Int = 1000): Observable<List<CurrentLocation>>

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
    @GET("cities/{city_id}/networks")
    fun getNearList(@Path(ParamsConstant.CityID) cityIDs: String,
                    @Query(ParamsConstant.Lat) lat: String,
                    @Query(ParamsConstant.Lng) lng: String,
                    @Query(ParamsConstant.Offset) offset: Int,
                    @Query(ParamsConstant.Limit) pageSize: Int,
                    @Query(ParamsConstant.CityID) cityID: String = cityIDs
    ): Observable<ArrayList<NearCarPoint>>

    /**
     * 附近车辆列表
     */
    @GET("xt/car_list")
    fun getCarList(): Observable<ArrayList<CarSelectInfo>>

    /**
     * 修改用户头像
     */
    @Multipart
    @POST("avatar")
    fun uploadAvatar(@Part image: List<MultipartBody.Part>): Observable<JsonObject>

    /**
     * 提交身份认证
     */
    @Multipart
    @POST("users/identity")
    fun uploadUserVerify(@Part(ParamsConstant.Name) name: RequestBody, @Part(ParamsConstant.IDcard) idCard: RequestBody, @Part image: List<MultipartBody.Part>): Observable<JsonObject>

    /**
     * 获取余额明细
     */
    @GET("users/bill")
    fun getRemainDetailList(@Query(ParamsConstant.Offset) offset: Int, @Query(ParamsConstant.Limit) pageSize: Int): Observable<List<RemainList>>

    /**
     * 获取支付方式
     * type：1充值2个人中心
     */
    @GET("users/pay/type")
    fun getPayMethod(@Query(ParamsConstant.Type) type: Int): Observable<List<PayMethod>>

    /**
     * 附近可用车辆
     */
    @GET("cars")
    fun getUsableCarList(@Query(ParamsConstant.CityID) cityID: String, @Query(ParamsConstant.NetWorkID) netId: String, @Query(ParamsConstant.Lat) lat: Double, @Query(ParamsConstant.Lng) lng: Double): Observable<List<CarInfo>>

    @GET("users/deposits")
    fun getPromiseMoney(): Observable<JsonObject>

    /**
     * 创建充值订单
     */
    @FormUrlEncoded
    @POST("users/deposits")
    fun createDeposits(@Field(ParamsConstant.UID) uid: String, @Field(ParamsConstant.CarModelID) carid: String? = null, @Field(ParamsConstant.PaymentID) payMethodID: String, @Field(ParamsConstant.Amount) amount: String): Observable<JsonObject>

    /**
     * 押金列表
     */
    @GET("users/deposits/logs")
    fun getDepositsLogs(@Query(ParamsConstant.Offset) offset: Int, @Query(ParamsConstant.Limit) pageSize: Int): Observable<List<PromiseMoneyDetail>>

    /**
     * 获取租车订单详情
     */
    @GET("users/deposits/logs")
    fun getRentOrderDetail(@Query(ParamsConstant.OrderId) order_id: String): Observable<RentOrderDetail>


    /**
     * 获取租车订单详情
     * status=1时，不用传orderid
     * 	status:状态（1寻车鸣笛、2开门、3锁车），固定传值为2
     */
    @FormUrlEncoded
    @POST("cars/{carID}")
    fun ringCar(@Path("carID") carId: String, @Query(ParamsConstant.OrderId) oid: String? = null, @Query(ParamsConstant.Status) status: String? = null): Observable<JsonObject>

    /**
     * 获取充值可用列表
     */
    @GET("users/deposits/logs")
    fun getPayAmountList(): Observable<List<PayAmount>>

    /**
     * 获取银行卡列表
     */
    @GET("users/bank_cards")
    fun getBankCardList(@Query(ParamsConstant.UID) uid: String): Observable<List<BankCard>>

    /**
     * 获取支持银行列表
     */
    @GET("banks")
    fun getBankTypedList(): Observable<List<BankType>>

    /**
     * 添加银行卡
     *
     */
    @FormUrlEncoded
    @POST("users/bank_cards")
    fun addBankCard(@Query(ParamsConstant.UID) uid: String, @Field(ParamsConstant.BankID) bankID: String, @Field(ParamsConstant.Bank) bank: String, @Field(ParamsConstant.AccountHolder) accountHolder: String, @Field(ParamsConstant.AccountNumber) accountNumber: String, @Field(ParamsConstant.Phone) phone: String, @Field(ParamsConstant.Default) default: String = "1"): Observable<JsonObject>

}