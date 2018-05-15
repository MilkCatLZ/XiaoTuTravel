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
     * shy.car.sdk.travel.order.take.ui.OrderTakeFragment
     */
    public static final String OrderTake = "/order/take";
    /**
     * shy.car.sdk.travel.order.take.ui.OrderTakeFragment
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
    @NotNull
    public static final String Setting = "/setting/main";
    @NotNull
    public static final String KeFu = "/kefu/main";
}
