package shy.car.sdk.app.route;


import org.jetbrains.annotations.NotNull;

import shy.car.sdk.travel.common.ui.GoodsTypeSelectDialogFragment;
import shy.car.sdk.travel.common.ui.SendTimeSelectDialogFragment;
import shy.car.sdk.travel.location.ui.LocationSelectActivity;
import shy.car.sdk.travel.login.ui.VerifyDialogFragment;
import shy.car.sdk.travel.pay.ui.CarTypeSelectActivity;
import shy.car.sdk.travel.pay.ui.PromiseMoneyDetailActivity;
import shy.car.sdk.travel.rent.ui.FindAndRentCarActivity;
import shy.car.sdk.travel.send.data.OrderSendList;
import shy.car.sdk.travel.send.ui.SendCitySmallPackageActivity;
import shy.car.sdk.travel.send.ui.SendHoleCarActivity;
import shy.car.sdk.travel.take.data.TakeOrderList;
import shy.car.sdk.travel.take.ui.OrderTakeDetailActivity;
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
     * shy.car.sdk.travel.message.ui.MessageActiveActivity
     */
    @NotNull
    public static final String MessageActive = "/message/active";
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
     * params:  withString("orderID",orderID)
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
     * 接单详情
     * {@link OrderTakeDetailActivity}
     * withObject(Object1,{@link TakeOrderList})
     */
    @NotNull
    public static final String OrderTakeDetail = "/take/take_order_detail";
    /**
     * 发货详情
     * {@link shy.car.sdk.travel.send.ui.OrderSendDetailActivity}
     * withObject(Object,{@link OrderSendList})
     */
    @NotNull
    public static final String OrderSendDetail = "/send/send_order_detail";
    /**
     * 接单详情fragment
     * {@link shy.car.sdk.travel.take.ui.OrderTakeDetailFragment}
     */
    @NotNull
    public static final String OrderTakeDetailFragment = "/take/take_order_detail_fragment";
    /**
     * 发货详情fragment
     * {@link shy.car.sdk.travel.send.ui.OrderSendDetailFragment}
     */
    @NotNull
    public static final String OrderSendDetailFragment = "/send/send_order_detail_fragment";
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

}
