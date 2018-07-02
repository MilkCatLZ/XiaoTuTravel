package shy.car.sdk.app.net

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.bank.data.BankType
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.coupon.data.Coupon
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.pay.data.PayAmount
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail
import shy.car.sdk.travel.remain.data.RemainList
import shy.car.sdk.travel.rent.data.BrokeType
import shy.car.sdk.travel.rent.data.CarCategory
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.send.data.CarUserTime
import shy.car.sdk.travel.send.data.OrderSendList
import shy.car.sdk.travel.take.data.DeliveryOrderList
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
     * 登出
     * JsonString：{"phone":"","verify":"","uuid":"",}
     */
    @DELETE("oauth/access_token")
//    @HTTP(method = "DELETE", path = "oauth/access_token", hasBody = true)
    fun logout(): Observable<Response<Void>>

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
     * type	integer	否	类型：1同城小包2整车物流
     */
    @GET("orders/freights")
    fun getOrderList(
            @Query(ParamsConstant.Type) type: String? = null,
            @Query(ParamsConstant.Offset) offset: Int,
            @Query(ParamsConstant.Limit) limit: Int): Observable<List<DeliveryOrderList>>

    /**
     * 获取单个接单详情
     *
     */
    @GET("orders/freights/{freight_id}")
    fun getOrderDetail(@Path(ParamsConstant.FreightID) fid: String): Observable<DeliveryOrderDetail>

    /**
     * 获取发货列表
     *
     */
    @GET("orders/freights")
    fun getOrderSendList(
            @Query(ParamsConstant.Type) type: String = "2",
            @Query(ParamsConstant.Offset) offset: Int,
            @Query(ParamsConstant.Limit) limit: Int): Observable<List<OrderSendList>>

    /**
     * 获取发货列表
     *
     */
    @GET("orders")
    fun getOrderMineList(
            @Query(ParamsConstant.Status) status: String = "0",
            @Query(ParamsConstant.Type) type: String,
            @Query(ParamsConstant.Offset) offset: Int,
            @Query(ParamsConstant.Limit) limit: Int): Observable<List<OrderMineList>>

    /**
     * 获取租车订单列表
     *
     */
    @GET("orders")
    fun getRentOrderList(
            @Query(ParamsConstant.Type) type: String,
            @Query(ParamsConstant.Status) status: String,
            @Query(ParamsConstant.Offset) offset: Int,
            @Query(ParamsConstant.Limit) limit: Int): Observable<List<OrderMineList>>

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
    fun getMessageList(
            @Query(ParamsConstant.PageIndex) pageIndex: Int,
            @Query(ParamsConstant.PageSize) pageSize: Int): Observable<ArrayList<MessageList>>

    /**
     * 附近网点列表
     */
    @GET("cities/{city_id}/networks")
    fun getNearList(@Path(ParamsConstant.CityID) cityIDs: String,
                    @Query(ParamsConstant.Lat) lat: Double? = null,
                    @Query(ParamsConstant.Lng) lng: Double? = null,
                    @Query(ParamsConstant.Offset) offset: Int? = null,
                    @Query(ParamsConstant.Limit) pageSize: Int? = null,
                    @Query(ParamsConstant.CarModelID) car: String? = null,
                    @Query(ParamsConstant.GetMode) mode: String = "1",
                    @Query(ParamsConstant.CityID) cityID: String = cityIDs
    ): Observable<ArrayList<NearCarPoint>>

    /**
     * 附近车辆列表
     */
    @GET("users/deposits/car")
    fun getCarList(): Observable<ArrayList<CarSelectInfo>>

    /**
     * 修改用户头像
     */
    @Multipart
    @POST("users/info")
    fun uploadUserDetail(@Part(ParamsConstant.NickName) nickName: RequestBody? = null,
                         @Part(ParamsConstant.Sex) sex: RequestBody? = null,
                         @Part(ParamsConstant.Birthday) birthday: RequestBody? = null,
                         @Part(ParamsConstant.City) city: RequestBody? = null,
                         @Part(ParamsConstant.Profession) profession: RequestBody? = null,
                         @Part image: List<MultipartBody.Part>? = null): Observable<JsonObject>

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
    fun getUsableCarList(@Query(ParamsConstant.CityID) cityID: String,
                         @Query(ParamsConstant.NetWorkID) netId: String? = null,
                         @Query(ParamsConstant.Lat) lat: Double,
                         @Query(ParamsConstant.Lng) lng: Double,
                         @Query(ParamsConstant.ModelID) modelID: String): Observable<List<CarInfo>>

    @GET("users/deposits")
    fun getPromiseMoney(): Observable<JsonObject>

    /**
     * 支付押金
     */
    @FormUrlEncoded
    @POST("users/deposits")
    fun createDeposits(@Query(ParamsConstant.UID) uid: String, @Field(ParamsConstant.CarModelID) carid: String? = null, @Field(ParamsConstant.PaymentID) payMethodID: String, @Field(ParamsConstant.Amount) amount: String?): Observable<JsonObject>

    /**
     * 充值
     */
    @FormUrlEncoded
    @PATCH("balance/recharge")
    fun createRecharge(@Field(ParamsConstant.Amount) amount: String, @Field(ParamsConstant.PaymentID) payMethodID: String): Observable<JsonObject>

    /**
     * 押金列表
     */
    @GET("users/deposits/logs")
    fun getDepositsLogs(@Query(ParamsConstant.Offset) offset: Int, @Query(ParamsConstant.Limit) pageSize: Int): Observable<List<PromiseMoneyDetail>>

    /**
     * 获取租车订单详情
     */
    @GET("orders/{order_id}")
    fun getRentOrderDetail(@Path(ParamsConstant.OrderId) oid: String): Observable<RentOrderDetail>


    /**
     * 操作车辆
     * 	status:状态（1寻车鸣笛、2开门、3锁车）
     */
    @FormUrlEncoded
    @POST("cars/{carID}")
    fun carAction(@Path("carID") carId: String, @Query(ParamsConstant.CarID) carid: String = carId, @Field(ParamsConstant.OrderId) oid: String? = null, @Field(ParamsConstant.Status) status: String? = null): Observable<JsonObject>

    /**
     * 获取充值可用列表
     */
    @GET("balance/package")
    fun getPayAmountList(): Observable<List<PayAmount>>

    /**
     * 获取银行卡列表
     */
    @GET("users/bank_cards")
    fun getBankCardList(@Query(ParamsConstant.UID) uid: String): Observable<List<BankCard>>

    /**
     * 获取优惠券列表
     */
    @GET("coupons")
    fun getCouponList(@Query(ParamsConstant.Offset) offset: Int = 0, @Query(ParamsConstant.Limit) limit: Int = 10, @Query(ParamsConstant.Status) status: String? = null): Observable<List<Coupon>>

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

    /**
     * 创建租车订单
     * type=1 为租车
     */
    @FormUrlEncoded
    @POST("orders")
    fun createRentCarOrder(@Field(ParamsConstant.CarID) carID: String, @Field(ParamsConstant.NetWorkID) networkID: String, @Field(ParamsConstant.Type) type: String = "1"): Observable<JsonObject>

    /**
     * 创建租车订单
    city	integer	是	服务城市ID
    model_id	integer	是	车型（整车发布时必须，同城不填）
    use_car_at_strart	string	是	用车时段开始
    use_car_at_end	string	是	用车时段结束（全天传0）
    from_address	string	是	出发地址
    from_lng	float	是	出发经度
    from_lat	float	是	出发纬度
    to_address	string	是	到达地址
    to_lng	float	是	达到经度
    to_lat	float	是	达到纬度
    type	integer	是	货运类型：1同城小包2整车物流
    freight_type	integer	是	货物类型
    freight_other	string	是	货物类型其它添加
    freight	float	是	运费
    weight	float	是	重量：单位千克
    volume	float	是	体积：单位立方
    remark	string	是	备注
     */
    @FormUrlEncoded
    @POST("orders/freights")
    fun postDeliveryOrder(
            @Field(ParamsConstant.City) cityID: String,
            @Field(ParamsConstant.ModelID) carID: String? = null,
            @Field(ParamsConstant.UseCarStart) use_car_at_strart: String,
            @Field(ParamsConstant.UseCarEnd) use_car_at_end: String,
            @Field(ParamsConstant.FromAddress) from_address: String,
            @Field(ParamsConstant.FromLng) from_lng: String,
            @Field(ParamsConstant.FromLat) from_lat: String,
            @Field(ParamsConstant.ToAddress) to_address: String,
            @Field(ParamsConstant.ToLng) to_lng: String,
            @Field(ParamsConstant.ToLat) to_lat: String,
            @Field(ParamsConstant.Type) type: String,
            @Field(ParamsConstant.FreightType) freight_type: String? = null,
            @Field(ParamsConstant.FreightOther) freight_other: String,
            @Field(ParamsConstant.Freight) freight: String,
            @Field(ParamsConstant.Weight) weight: String? = null,
            @Field(ParamsConstant.Volume) volume: String? = null,
            @Field(ParamsConstant.Remark) remark: String? = null
    ): Observable<JsonObject>

    @DELETE("orders/{order_id}")
    fun cancelRentOrder(@Path(ParamsConstant.OrderId) order_id: String, @Query(ParamsConstant.OrderId) oid: String = order_id): Observable<Response<Void>>

    @GET("orders/freights/car")
    fun getCarTypeList(): Observable<List<shy.car.sdk.travel.send.data.CarInfo>>

    @GET("orders/freights/time")
    fun getCarUseTime(): Observable<List<CarUserTime>>

    @FormUrlEncoded
    @PATCH("orders/{order_id}")
    fun orderUnLockCarAndStart(@Path(ParamsConstant.OrderId) oid: String, @Field(ParamsConstant.OrderStatus) status: String = "3"/*, @Part image: List<MultipartBody.Part>? = null*/): Observable<JsonObject>

    /**
     * 提交身份认证
     */
    @Multipart
    @POST("orders/{oid}/photos")
    fun uploadCarPic(@Path("oid") order_id: String,
                     @Query(ParamsConstant.OrderId) oid: String,
                     @Part(ParamsConstant.Type) type: RequestBody,
                     @Part image: List<MultipartBody.Part>): Observable<JsonObject>

    @GET("orders/freights/type")
    fun getFreightTypeList(): Observable<List<GoodsType>>

    @FormUrlEncoded
    @PATCH("orders/freights/{freight_id}")
    fun takeDeliveryOrder(@Path("freight_id") freight_id: String, @Field("freight_status") status: String = "1"): Observable<JsonObject>


    @DELETE("orders/freights/{freight_id}")
    fun cancelOrder(@Path("freight_id") freight_id: String): Observable<Response<Void>>

    @FormUrlEncoded
    @PATCH("orders/freights/{freight_id}")
    fun deliveryFinish(@Path("freight_id") id: String, @Field("freight_status") status: String = "4"): Observable<JsonObject>

    @FormUrlEncoded
    @PATCH("balance")
    fun tixian(@Field(ParamsConstant.BankCardID) bankCardID: String, @Field(ParamsConstant.Amount) amount: String, @Field(ParamsConstant.Action) action: String = "1"): Observable<JsonObject>

    @FormUrlEncoded
    @PATCH("orders/{order_id}")
    fun returnCar(@Path(ParamsConstant.OrderId) oid: String, @Field(ParamsConstant.NetWorkID) networkID: String, @Field(ParamsConstant.Lat) lat: String, @Field(ParamsConstant.Lng) lng: String, @Field(ParamsConstant.OrderStatus) orderStatus: String = "4"): Observable<JsonObject>

    /**
     * 	status：1取车 2还车
     */
    @GET("orders/photos")
    fun shoulTakePhoto(@Query(ParamsConstant.Type) status: String): Observable<JsonObject>

    @GET("cars/model")
    fun getUsableCarModelList(): Observable<List<CarCategory>>

    @FormUrlEncoded
    @PATCH("orders/{order_id}")
    fun payOrder(@Path(ParamsConstant.OrderId) oid: String, @Field(ParamsConstant.PaymentID) payment: String, @Field(ParamsConstant.CouponID) coupon: String? = null, @Field(ParamsConstant.OrderStatus) orderStatus: String = "5"): Observable<JsonObject>

    @GET("cars/notice/type")
    fun getBrokeType(): Observable<List<BrokeType>>

    /**
     * 修改用户头像
     */
    @Multipart
    @POST("cars/notice")
    fun uploadCarBroke(@Part(ParamsConstant.CarID) carid: RequestBody? = null,
                         @Part(ParamsConstant.Type) type: RequestBody? = null,
                         @Part(ParamsConstant.Content) content: RequestBody? = null,
                         @Part image: List<MultipartBody.Part>? = null): Observable<JsonObject>

}