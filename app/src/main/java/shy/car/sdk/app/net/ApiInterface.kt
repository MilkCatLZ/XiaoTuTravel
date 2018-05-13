package shy.car.sdk.app.net

import io.reactivex.Observable
import retrofit2.http.*
import shy.car.sdk.travel.location.data.City


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
    fun gerVerify(@Query("phone") params: String): Observable<String>

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
}