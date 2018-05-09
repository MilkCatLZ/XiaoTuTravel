package shy.car.sdk.travel.login.data


import com.alibaba.fastjson.annotation.JSONField


/**
 * Created by LZ on 2017/10/20.
 */

class LoginState {


    /**
     * uid : 11094
     * type : 1
     * status : 4
     * first_time : 0
     * access_token : $2y$08$TnJEK0ZQY3h4aVVBMno0ZeQA5kU2Jf/AKMl6/FZbsj.aPM2Fh8NRW
     * expires_in : 2592000
     */

    @JSONField(name = "uid")
    var uid: String? = null
    @JSONField(name = "type")
    var type: Int = 0
    @JSONField(name = "status")
    var status: Int = 0
    @JSONField(name = "first_time")
    var firstTime: Int = 0
    @JSONField(name = "access_token")
    var accessToken: String? = null
    @JSONField(name = "expires_in")
    var expiresIn: Long = 0
}
