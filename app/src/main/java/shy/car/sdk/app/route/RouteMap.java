package shy.car.sdk.app.route;


import org.jetbrains.annotations.NotNull;

import shy.car.sdk.travel.bank.data.BankCard;
import shy.car.sdk.travel.bank.ui.BankCardManagerActivity;
import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment;
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment;
import shy.car.sdk.travel.coupon.ui.CouPonActivity;
import shy.car.sdk.travel.location.ui.LocationSelectActivity;
import shy.car.sdk.travel.login.ui.VerifyDialogFragment;
import shy.car.sdk.travel.order.data.OrderMineList;
import shy.car.sdk.travel.order.ui.OrderDetailActivity;
import shy.car.sdk.travel.pay.ui.CarTypeSelectActivity;
import shy.car.sdk.travel.pay.ui.OrderPayActivity;
import shy.car.sdk.travel.pay.ui.PromiseMoneyDetailActivity;
import shy.car.sdk.travel.pay.ui.PromiseMoneyPaySuccessActivity;
import shy.car.sdk.travel.pay.ui.PromiseMoneyReturnActivity;
import shy.car.sdk.travel.pay.ui.RentPaySuccessActivity;
import shy.car.sdk.travel.remain.ui.TiXianActivity;
import shy.car.sdk.travel.rent.ui.CarBrokeUploadActivity;
import shy.car.sdk.travel.rent.ui.DrivingActivity;
import shy.car.sdk.travel.rent.ui.FindAndRentCarActivity;
import shy.car.sdk.travel.rent.ui.InsuranceProcessActivity;
import shy.car.sdk.travel.rent.ui.ReturnAreaActivity;
import shy.car.sdk.travel.rent.ui.ReturnCarActivity;
import shy.car.sdk.travel.rent.ui.ReturnCarFragment;
import shy.car.sdk.travel.rent.ui.UnLockCarActivity;
import shy.car.sdk.travel.send.ui.SendCitySmallPackageActivity;
import shy.car.sdk.travel.send.ui.SendHoleCarActivity;
import shy.car.sdk.travel.user.ui.UserInfoEditActivity;


public class RouteMap {
    /**
     * shy.car.sdk.MainActivity
     */
    public static final String Home = "/app/homeActivity";
    /**
     * {@link * shy.car.sdk.travel.login.ui.LoginDialogFragment}
     */
    public static final String Login = "/user/login";
    /**
     * 输入验证码
     * {@link VerifyDialogFragment}
     * withString(ParamsConstant.String1, phone).withInt(ParamsConstant.Int1,interval)
     */
    public static final String Verify = "/user/verify";
    /**
     * {@link * shy.car.sdk.travel.take.ui.OrderTakeFragment}
     */
    public static final String OrderTake = "/order/take";
    /**
     * {@link * shy.car.sdk.travel.take.ui.OrderTakeFragment}
     */
    public static final String OrderSend = "/order/send";
    /**
     * shy.car.sdk.travel.rent.ui.CarRentFragment
     */
    public static final String CarRent = "/car/rent";
    /**
     * shy.car.sdk.travel.wallet.ui.WalletActivity
     */
    public static final String Wallet = "/wallet/main";
    
    @NotNull
    public static final String UserDetail = "/user/detail";
    
    /**
     * shy.car.sdk.travel.setting.ui.SettingActivity
     */
    @NotNull
    public static final String Setting = "/setting/main";
    /**
     * shy.car.sdk.travel.setting.ui.KeFuActivity
     */
    @NotNull
    public static final String KeFu = "/kefu/main";
    /**
     * shy.car.sdk.travel.order.ui.OrderMineActivity
     */
    @NotNull
    public static final String OrderMine = "/order/mine";
    /**
     * {@link shy.car.sdk.travel.message.ui.MessageActiveActivity}
     * 活动消息
     */
    @NotNull
    public static final String MessageActive = "/message/active";
    /**
     * {@link shy.car.sdk.travel.message.ui.MessageServiceActivity}
     * 服务消息
     */
    @NotNull
    public static final String MessageService = "/message/service";
    /**
     * {@link shy.car.sdk.travel.message.ui.MessageSystemActivity}
     * 系統消息
     */
    @NotNull
    public static final String MessageSystem = "/message/system";
    /**
     * shy.car.sdk.travel.message.ui.MessageActiveActivity
     */
    @NotNull
    public static final String MessageCenter = "/message/center";
    /**
     * {@link shy.car.sdk.travel.dialog.DialogMoneyVerify}
     */
    @NotNull
    public static final String Dialog_Money_Verify = "/dialog/money_verify";
    /**
     * 交保证金
     */
    @NotNull
    public static final String MoneyVerify = "/money/verify";
    /**
     * 站点信息
     */
    @NotNull
    public static final String CarPointDetail = "/rent/car_point_detail";
    
    /**
     * 租车详情
     * params:  withString(ParamsConstant.String1,orderID)
     */
    @NotNull
    public static final String RentCarDetail = "/rent/rent_car_detail";
    
    /**
     * 支付方式选择
     * {@link * shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog}
     * withObject(Object1,{@link shy.car.sdk.travel.pay.data.PayMethod})
     * withInt(Int1,1:充值 2:个人中心)
     */
    @NotNull
    public static final String PaySelect = "/pay/select";
    /**
     * 选择车辆
     * {@link CarTypeSelectActivity}
     */
    @NotNull
    public static final String CarTypeSelect = "/common/car_type_select";
    /**
     * 保证金明细
     * {@link PromiseMoneyDetailActivity}
     */
    @NotNull
    public static final String PromiseMoneyDetail = "/common/promise_money_detail";
    /**
     * 验证用户
     * {@link shy.car.sdk.travel.user.ui.UserVerifyActivity}
     */
    @NotNull
    public static final String UserVerify = "/user/verify_user";
    /**
     * 货运订单详情
     * {@link OrderDetailActivity}
     * withObject(String1,oid)
     */
    @NotNull
    public static final String OrderDetail = "/take/take_order_detail";
    
    /**
     * 接单详情fragment
     * {@link shy.car.sdk.travel.order.ui.OrderDetailFragment}
     */
    @NotNull
    public static final String OrderTakeDetailFragment = "/take/take_order_detail_fragment";
    
    /**
     * 整车发货 填写发货信息
     * {@link SendHoleCarActivity}
     */
    @NotNull
    public static final String SendHoleCarEdit = "/send/send_hole_car_send_edit";
    /**
     * 选择地址
     * {@link LocationSelectActivity}
     */
    @NotNull
    public static final String LocationSelect = "/common/location_select";
    /**
     * 选择地址
     * {@link SendTimeSelectDialogFragment}
     */
    @NotNull
    public static final String SendTimeSelect = "/common/send_time_select";
    /**
     * 选择货物类型
     * {@link GoodsTypeSelectDialogFragment}
     */
    @NotNull
    public static final String GoodsTypeSelect = "/common/goods_type_select";
    /**
     * 选择同城小包
     * {@link SendCitySmallPackageActivity}
     */
    @NotNull
    public static final String SendCitySmallPackageSelect = "/send/city_small_package";
    /**
     * 找车取车
     * {@link FindAndRentCarActivity}
     * withString(Object1,{@link OrderMineList})
     */
    @NotNull
    public static final String FindAndRentCar = "/rent/find_and_rent";
    /**
     * 编辑资料
     * {@link UserInfoEditActivity}
     */
    @NotNull
    public static final String UserInfoEdit = "/user/user_info_edit";
    /**
     * 我的余额
     * {@link shy.car.sdk.travel.remain.ui.RemainDetailActivity}
     */
    @NotNull
    public static final String RemainDetail = "/remain/detail";
    /**
     * 充值
     * {@link shy.car.sdk.travel.pay.ui.PayActivity}
     */
    @NotNull
    public static final String Pay = "/pay/charge";
    /**
     * 银行卡
     * {@link BankCardManagerActivity}
     * withBoolean(ParamsConstant.Boolean1) true:选择模式；false:查看模式
     */
    @NotNull
    public static final String BankCard = "/bank/BankCard";
    /**
     * 添加银行卡
     * {@link BankCard}
     */
    @NotNull
    public static final String AddBankCard = "/bank/add";
    
    /**
     * 支付保证金成功
     * {@link PromiseMoneyPaySuccessActivity}
     */
    @NotNull
    public static final String PaySuccess = "/pay/success";
    
    /**
     * {@link UnLockCarActivity}
     */
    @NotNull
    public static final String UnLockCar = "/car/unlock";
    
    /**
     * {@link OrderPayActivity}
     * withString(String1,oid)
     */
    @NotNull
    public static final String OrderPay = "/pay/order";
    
    /**
     * {@link DrivingActivity}
     * 行程中
     * withString(String1,oid)
     */
    @NotNull
    public static final String Driving = "/rent/driving";
    
    /**
     * {@link TiXianActivity}
     * 提现
     */
    @NotNull
    public static final String TiXian = "/remain/tixian";
    
    /**
     * {@link ReturnCarActivity}
     * 还车
     * withString(String1,oid)
     */
    @NotNull
    public static final String ReturnCar = "/rent/return";
    
    /**
     * {@link ReturnCarFragment}
     * 还车并拍照
     * withString(String1,oid)
     */
    @NotNull
    public static final String ReturnCarUploadPhoto = "/rent/return_upload";
    
    /**
     * {@link TiXianActivity}
     * 提现
     * withString(String1,method) 提现方式
     * withString(String2,remain) 提现金额
     */
    @NotNull
    public static final String TiXianSuccess = "/remain/tixian_success";
    
    /**
     * {@link ReturnAreaActivity}
     * 还车区域
     */
    @NotNull
    public static final String ReturnArea = "/car/return_are";
    
    /**
     * {@link CouPonActivity}
     * 优惠券
     */
    @NotNull
    public static final String Coupon = "/coupon/list";
    
    /**
     * {@link PromiseMoneyReturnActivity}
     * 退还保证金
     */
    @NotNull
    public static final String ReturnPromiseMoney = "/pay/return_promise_money";
    
    /**
     * {@link RentPaySuccessActivity}
     * 租车支付成功
     */
    @NotNull
    public static final String RentCarPaySuccess = "/pay/rent_car_success";
    
    /**
     * {@link CarBrokeUploadActivity}
     * 故障上报
     * withObject(Object1,RentOrderDetail)
     */
    @NotNull
    public static final String CarBrokeUpLoad = "/rent/car_broke_upload";
    
    public static final String RegisterAgree = "/html/register_agree";
    public static final String ReturnCarAgree = "/html/return_car_agree";
    public static final String About = "/html/about";
    /**
     * 修改手机号
     * {@link shy.car.sdk.travel.login.ui.ChangeMobileActivity}
     */
    public static final String ChangeMobile = "/login/change_mobile";
    /**
     * 保证金退还申请成功
     * {@link shy.car.sdk.travel.pay.ui.ReturnPromiseMoneySuccessActivity}
     */
    public static final String ReturnPromiseMoneySuccess = "/pay/return_promise";
    /**
     * 保险流程
     * {@link InsuranceProcessActivity}
     */
    public static final String InsuranceProcess = "/rent/insurance_process";
    /**
     * 评价
     * {@link shy.car.sdk.travel.rent.ui.RentOrderCommentActivity}
     */
    public static final String RentComment = "/rent/comment";
    /**
     * 评价
     * {@link shy.car.sdk.travel.user.ui.VipActivity}
     */
    public static final String Vip = "/user/vip";
    
    /**
     * 发票列表
     * {@link shy.car.sdk.travel.invoice.ui.InvoiceListActivity}
     */
    public static final String InvoiceList = "/invoice/list";
    
    /**
     * 提交开票申请
     * {@link shy.car.sdk.travel.invoice.ui.InvoicePostActivity}
     * withString（String1,Gson().toJson(ArrayList<InvoiceList.Orders>)）
     * withObject1（Object1,ArrayList<InvoiceList.Orders>）
     */
    public static final String InvoicePost = "/invoice/post";
    
    public static final String FeedBack = "/setting/feedback";
}
