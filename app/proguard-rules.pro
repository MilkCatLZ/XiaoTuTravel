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

#okhttp
-keep class com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

#picasso
-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.picasso.**

#支付宝
-keep class com.alipay.** { *; }
-dontwarn com.alipay.**

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

-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class shy.car.sdk.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class okhttp3.** {*;}
-dontwarn okhttp3.**

-keep class okio.** {*;}
-dontwarn okio.**

-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

#AROUTER 路由表
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider