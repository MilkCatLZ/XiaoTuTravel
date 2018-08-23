package shy.car.sdk.travel.message.data;

import com.google.gson.annotations.SerializedName;

public class MessageInfo {

    /**
     * activity : {"name":"活动消息","lastnew":""}
     * service : {"name":"服务消息","lastnew":"货运资格证认证通知"}
     * system : {"name":"系统消息","lastnew":"2222"}
     */

    @SerializedName("activity")
    private ActivityBean activity;
    @SerializedName("service")
    private ServiceBean service;
    @SerializedName("system")
    private SystemBean system;

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public SystemBean getSystem() {
        return system;
    }

    public void setSystem(SystemBean system) {
        this.system = system;
    }

    public static class ActivityBean {
        /**
         * name : 活动消息
         * lastnew :
         */

        @SerializedName("name")
        private String name;
        @SerializedName("lastnew")
        private String lastnew;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastnew() {
            return lastnew;
        }

        public void setLastnew(String lastnew) {
            this.lastnew = lastnew;
        }
    }

    public static class ServiceBean {
        /**
         * name : 服务消息
         * lastnew : 货运资格证认证通知
         */

        @SerializedName("name")
        private String name;
        @SerializedName("lastnew")
        private String lastnew;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastnew() {
            return lastnew;
        }

        public void setLastnew(String lastnew) {
            this.lastnew = lastnew;
        }
    }

    public static class SystemBean {
        /**
         * name : 系统消息
         * lastnew : 2222
         */

        @SerializedName("name")
        private String name;
        @SerializedName("lastnew")
        private String lastnew;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastnew() {
            return lastnew;
        }

        public void setLastnew(String lastnew) {
            this.lastnew = lastnew;
        }
    }
}
