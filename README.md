# Frame-MMVM

Frame-MMVM框架采用的是Kotlin、DataBinding、LiveDate搭建的一套框架，里面已经封装好了网络请求，支持多套域名，不同域名可以设置自己的加验方式。

### 使用方法

* 实现RequestHandler处理请求前和请求后的处理方式。

* 实现NetProvider配置一些公共请求参数，比如请求头，请求超时时间。
* BaseResult是前后端定义的返回参数结构。
* BaseViewModel是网络请求的基础类，所有的ViewModel请求都要依赖它。
* ApiCallback是返回参数统一处理类，这里面可以处理统一逻辑，比如退出登录。

* 使用方式只需要继承BaseActivity和BaseFragment即可，要传入的两个参数分别为DataBinding和LiveModel的类。
