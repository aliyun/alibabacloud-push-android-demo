# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/liuzhilong/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-keep class org.json.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**
-dontwarn com.huawei.**

# 小米通道 https://dev.mi.com/console/doc/detail?pId=41#_1_1
-keep class com.xiaomi.** {*;}
-dontwarn com.xiaomi.**

# 华为通道 https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/android-config-obfuscation-scripts-0000001050176973
-keep class com.huawei.** {*;}
-dontwarn com.huawei.**

# VIVO通道 https://dev.vivo.com.cn/documentCenter/doc/365#w1-93724785
-keep class com.vivo.** {*;}
-dontwarn com.vivo.**

# OPPO通道 https://open.oppomobile.com/wiki/doc#id=11221
-keep public class * extends android.app.Service
-keep class com.heytap.msp.** { *;}

# 魅族通道 http://open.res.flyme.cn/fileserver/upload/file/202109/7bf101e2843642709c7a11f4b57861cd.pdf 没有相关说明
-keep class com.meizu.cloud.** {*;}
-dontwarn com.meizu.cloud.**

# GCM/FCM通道
-keep class com.google.firebase.**{*;}
-dontwarn com.google.firebase.**
