package shy.car.sdk.travel.user.data;

public class UserState {

    public static class UserIdentityAuth {
        /**
         * 未认证
         */
        public static final int NoIdentity = 0;
        /**
         * 认证中
         */
        public static final int Identiting = 1;
        /**
         * 已认证
         */
        public static final int Identited = 2;
    }
    public static class UserDeposit {
        /**
         * 未缴纳
         */
        public static final int NoDeposit = 0;
        /**
         * 已缴纳
         */
        public static final int Deposited = 1;
    }
}
