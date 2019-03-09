# JPermission

#### 此项目是Android开发中动态权限申请框架，AOP切面方式，使用AspectJ切入来实现权限的申请和相关操作。


## 更新

#### v1.0.5
###### 取消无用日志的打印，精简项目

#### v1.0.2
###### 解决在kotlin中使用的问题 感谢大佬的AOP框架[gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)

## 使用方法
### 1、引入库
  ###### 首先在项目根目录的build.gradle里依赖AspectJX
```
dependencies {
   classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
}
```
###### 然后在app项目的build.gradle里应用插件
```
apply plugin: 'android-aspectjx'
//或者
apply plugin: 'com.hujiang.android-aspectjx'
```

###### 最后在需要接入JPermission的module的Gradle中添加
```
implementation 'org.liang.library:jpermission:1.0.2'
```


### 2. 申请权限

##### 在需要获取到权限才能执行的方法上边使用注解@Permission传入权限

```
@Permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
fun testPermission() {
    Toast.makeText(this, "已获得所有权限", Toast.LENGTH_LONG).show()
}
```
##### 申请结果监听
```
//用户点击拒绝并不再提醒
@PermissionBanned
fun permissionBanned(permissions: Any) {
    Log.e("TestActivity", "已拒绝: " + (permissions as Array<String>).size)
}

//用户只点击拒绝，相当于取消申请权限
@PermissionDenied
fun permissionDenied(permissions: Any) {
    Log.e("TestActivity", "取消申请: " + (permissions as Array<String>).size) 
}
```
### 最后别忘了AndroidManifest中的权限配置哦

## 参考
#### [gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
#### [支持kotlin代码织入的AspectJ Demo](https://github.com/HujiangTechnology/AspectJX-Demo)
#### [用aspectjx实现的简单、方便、省事的Android M动态权限配置框架](https://github.com/firefly1126/android_permission_aspectjx)


