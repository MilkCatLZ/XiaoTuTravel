package shy.car.sdk.app.route;


import org.jetbrains.annotations.NotNull;


public class RouteMap {
    /**
     * shy.car.sdk.MainActivity
     */
    public static final String Home = "/app/homeActivity";
    /**
     * shy.car.sdk.travel.login.ui.LoginDialogFragment
     */
    public static final String Login = "/user/login";
    /**
     * shy.car.sdk.travel.login.ui.VerifyDialogFragment
     */
    public static final String Verify = "/user/verify";
    /**
     * shy.car.sdk.travel.take.ui.OrderTakeFragment
     */
    public static final String OrderTake = "/order/take";
    /**
     * shy.car.sdk.travel.take.ui.OrderTakeFragment
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
     * shy.car.sdk.travel.dialog.DialogMoneyVerify
     */
    @NotNull
    public static final String Dialog_Money_Verify = "/dialog/money_verify";
    /**
     * 交保证金
     */
    @NotNull
    public static final String VerifyMoney = "/money/verify";
    /**
     * 站点信息
     */
    @NotNull
    public static final String CarPointDetail = "/rent/car_point_detail";

    /**
     * 租车详情
     */
    @NotNull
    public static final String RentCarDetail = "/rent/rent_car_detail";

    /**
     * 租车详情
     */
    @NotNull
    public static final String PaySelect = "/pay/select";

}
