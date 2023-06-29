# 阿里云移动推送官方全新Demo Android版（Kotlin + MVVM)
[![maven version](https://img.shields.io/badge/Maven-3.0.11-brightgreen.svg)](https://mhub.console.aliyun.com/#/download) [![platform](https://img.shields.io/badge/platform-android-lightgrey.svg)](https://developer.android.com/index.html) [![language](https://img.shields.io/badge/language-kotlin-orange.svg)](https://kotlinlang.org/) [![website](https://img.shields.io/badge/website-CloudPush-red.svg)](https://www.aliyun.com/product/cps)


<div align="center">
<img src="logo.png">
</div>

阿里移动推送（Alibaba Cloud Mobile Push）是基于大数据的移动智能推送服务，帮助App快速集成移动推送的功能，在实现高效、精确、实时的移动推送的同时，极大地降低了开发成本。让开发者最有效地与用户保持连接，从而提高用户活跃度、提高应用的留存率。

## 产品特性

-   *高效稳定*——与手机淘宝使用相同架构，基于阿里集团高可用通道。该通道日均消息发送量可达30亿，目前活跃使用的用户达1.8亿。
-   *高到达率*——Android 智能通道保活，多通道支持保证推送高到达率。
-   *精确推送*——基于阿里大数据处理技术，实现精确推送。
-   *应用内消息推送*——支持 Android 与 iOS 应用内私有通道，保证透传消息高速抵达。


## 使用方法

### 1. 在EMAS控制台创建应用

在EMAS控制台创建应用可参见[快速入门](https://help.aliyun.com/document_detail/436513.htm?spm=a2c4g.434639.0.0.62963e06nJThuT#topic-2225340)

### 2. 下载Demo工程

将工程克隆或下载到本地：

```shell
git clone https://github.com/aliyun/alicloud-android-demo.git
```

**当您在使用您自己的APP集成移动推送遇到问题时，您可以对比下Demo的配置情况**

>[Android SDK配置文档](https://help.aliyun.com/document_detail/51056.html)


###  3. 基础配置

#### 3.1 配置AppKey、AppSecret

为了使Demo APP能够正常运行，您还需要配置您的appkey/appsecret信息。

在`AndroidManifest.xml`代码片段中用您的appkey/appsecret替换`******`字段占据的参数

```xml
 <!-- 请填写你自己的- appKey -->
 <meta-data 
 	android:name="com.alibaba.app.appkey" 
 	android:value="*****"/> 
 <!-- 请填写你自己的appSecret -->
 <meta-data 
 	android:name="com.alibaba.app.appsecret" 
 	android:value="******"/> 
```

#### 3.2 配置辅助通道

辅助通道的配置分为两类：

+ 华为、荣耀、VIVO需要在`AndroidManifest.xml`中进行配置，将`******`替换为您自己的辅助通道参数

```xml
<!-- 华为通道的参数appid -->
<meta-data
	android:name="com.huawei.hms.client.appid"
	android:value="appid=******" />
<!-- vivo通道的参数api_key为appkey -->
<meta-data
	android:name="com.vivo.push.api_key"
	android:value="******" />
<meta-data
	android:name="com.vivo.push.app_id"
	android:value="******" />
<!-- honor通道的参数 -->
<meta-data
	android:name="com.hihonor.push.app_id"
	android:value="******" />
```

+ 小米、魅族、OPPO、GCM需要在`app/build.gradle`中进行配置

```groovy
//小米
buildConfigField "String", "MIPUSH_ID", "\"*******\""
buildConfigField "String", "MIPUSH_KEY", "\"*******\""

//OPPO
buildConfigField "String", "OPPO_APP_KEY", "\"*******\""
buildConfigField "String", "OPPO_APP_SECRET", "\"*******\""
        
//MEIZU
buildConfigField "String", "MEIZU_APP_ID", "\"*******\""
buildConfigField "String", "MEIZU_APP_KEY", "\"*******\""
       
//GCM
buildConfigField "String", "GCM_SEND_ID", "\"*******\""
buildConfigField "String", "GCM_APPLICATION_ID", "\"*******\""
buildConfigField "String", "GCM_PROJECT_ID", "\"*******\""
buildConfigField "String", "GCM_API_KEY", "\"*******\""
```


#### 3.2 配置包名

将`build.gradle`文件中的`namespace`和`applicationId`参数改成所创建App的包名：

```groovy
android {
	namespace '********'
	compileSdk 33
   
    defaultConfig {
        applicationId "********" // 填写所创建App的包名
        minSdk 21
        targetSdk 33
        versionCode 1
        versionName "1.0"
    }
}
```



### 4. 编译Demo

```
./gradlew -p app clean build
```

## 联系我们

-   官网：[移动推送](https://www.aliyun.com/product/cps)
-   钉钉技术支持：11795523（钉钉群号）
-   官方技术博客：[阿里云移动服务](https://yq.aliyun.com/teams/32)