# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-skipnonpubliclibraryclasses
-printusage
-dontskipnonpubliclibraryclasses
-optimizations !code/allocation/variable

-keepparameternames
-keepattributes Signature

-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable

#保存并复用名称映射
#-applymapping ./build/outputs/mapping/debug/mapping.txt
-printmapping

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#glide
-keep,includedescriptorclasses public class * implements com.bumptech.glide.module.GlideModule
-keep,includedescriptorclasses public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#保持所有实现了Serializable的类的关键内容
-keepclassmembers,includedescriptorclasses class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持所有用到了的Serializable的类和它们的名称
-keepnames class * implements java.io.Serializable

##保持所有实现了JSONAware的类的关键内容
#-keepclassmembers,includedescriptorclasses class * implements com.alibaba.fastjson.JSONAware {
#    public *;
#}
##保持所有支持FastJSON的类的关键内容
#-keepclassmembers,includedescriptorclasses class ** {
#    @com.alibaba.fastjson.annotation.JSONField <fields>;
#    @com.alibaba.fastjson.annotation.JSONField <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <init>(...);
#    !private <init>();
#    public final <fields>;
#    public void set*(***);
#    public boolean is*();
#    public *** get*();
#}
##保持所有用到了的支持FastJSON的类
#-keepclasseswithmembernames,includedescriptorclasses class ** {
#    @com.alibaba.fastjson.annotation.JSONField <fields>;
#    @com.alibaba.fastjson.annotation.JSONField <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <init>(...);
#    !private <init>();
#    public final <fields>;
#    public void set*(***);
#    public boolean is*();
#    public *** get*();
#}
#-keepnames,includedescriptorclasses class ** {
#    @com.alibaba.fastjson.annotation.JSONField <fields>;
#    @com.alibaba.fastjson.annotation.JSONField <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <methods>;
#    @com.alibaba.fastjson.annotation.JSONCreator !private <init>(...);
#    !private <init>();
#    public final <fields>;
#    public void set*(***);
#    public boolean is*();
#    public *** get*();
#}

#保持所有用到了的native方法以及方法所在的类
#但是如果一个类里面所有的native方法一个都没用到，就允许把相应的类优化掉
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

#保持所有需要保持的类成员，同时允许优化掉完全没有被用掉的类。
-keepclassmembers,includedescriptorclasses class ** {
    @android.support.annotation.Keep *;
}

#保持所有支持EventBus的内容，同时允许优化掉完全没有被用到的类。
-keepclassmembers,includedescriptorclasses class ** {
    public void onEvent*(**);
}

#保持所有支持DataBinding的内容，同时允许优化掉完全没有被用到的类。
-keepclassmembers,includedescriptorclasses class ** {
    @android.databinding.Bindable *;
}

#保持所有支持DataBinding的内容，同时允许优化掉完全没有被用到的类。
-keepclasseswithmembernames,includedescriptorclasses class ** extends android.databinding.ViewDataBinding {
    !private *;
}

#保持所有枚举
-keepclasseswithmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保持所有的LayoutManager，因为它们可以在xml里被实例化。
-keep,includedescriptorclasses class ** extends android.support.v7.widget.RecyclerView$LayoutManager {
    !private *;
}

#保持所有R的内容
-keep class **.R$** {
    !private *;
}

#保持所有BR的内容
-keep class **.BR$** {
    !private *;
}

#保持SharedPreferenceEditor.apply()
-keepclassmembers,includedescriptorclasses class * implements android.content.SharedPreferences$Editor {
    !private *;
}

#android.LNDataBindingAdapter.BindingBuildInfo
-dontwarn android.databinding.**
-keep,includedescriptorclasses class android.databinding.** { *; }
-keepclasseswithmembers,includedescriptorclasses class ** {
    @android.databinding.BindingAdapter public static void <methods>;
}



##fastjson
#-keep class com.alibaba.fastjson.** { *; }
#-dontwarn com.alibaba.fastjson.**


-keep class okhttp3.** {*;}
-dontwarn okhttp3.**

-keep class okio.** {*;}
-dontwarn okio.**

-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

#picasso
-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.picasso.**

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


#ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class com.alibaba.android.arouter.facade.model.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider



#start-------------------------------------------支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
#end----------------------------------------支付宝

#start-------------------------------------------umeng Push
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**
-dontwarn com.alipay.sdk.**
-dontwarn com.ta.utdid2.**
-keepattributes *Annotation*
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}
-keep public class **.R$*{
   public static final int *;
}
#end----------------------------------------umeng Push

#start-------------------------------------------umeng Share
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature
#end----------------------------------------umeng Share


#start--------other
-keep class javax.lang.model.element.** {*;}
-dontwarn com.alibaba.android.arouter.facade.model.**

-keep class com.alipay.android.phone.mrpc.core.** {*;}
-dontwarn com.alipay.android.phone.mrpc.core.**

#end----------other

#start--------高德地图
#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
-dontwarn com.amap.api.mapcore2d.**

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}


#内置语音 V5.6.0之后
-keep class com.alibaba.idst.nls.** {*;}
-keep class com.google.**{*;}
-keep class com.nlspeech.nlscodec.** {*;}

-keep class com.amap.api.col.** {*;}
-dontwarn com.amap.api.col.**


#end----------高德地图


