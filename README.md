# XiaoYunEC

3 概要设计
![.](https://raw.githubusercontent.com/aaLiweipeng/XiaoYunEC/master/xiaoyun_ec/src/main/res/mipmap-hdpi/ic_launcher.png)

<br>
<t>3.1 系统逻辑设计框架图


图 3.1 系统逻辑设计框架图

<br>
<t>3.2 系统功能汇总（功能使用流程图）

图3.2 系统功能汇总




4 详细设计与实现
<br>
<t>4.1 单Activity架构
项目所有界面都是使用Fragment（Delegate）来实现的，而所有的这些Fragment又交替地在同一个且唯一一个Activity上面展示，不同页面的切换，只是同一个Activity上的根Fragment的切换而已，即单Activity架构；
优势：Fragment比Activity要轻量级很多，使用以唯一一个Activity为基础、多个Fragment交替切换加载的单Activity架构，在性能上变现更加优良；
Fragment之间也可以相互嵌套，布局和业务逻辑可以处理得更加灵活；
各个Fragment都一个同一个Activity为基础，底层角度来看其通信上更加方便，而逻辑上又相互分工解耦，可读性强；

<br>
<t>4.2 Delegate顶层架构设计
使用面向对象编程特性，利用继承特性和Abstract关键字编程设计，构架基本架构体系，如下：
最顶层的BaseDelegate，是一个抽象类，抽象提取、封装了所有Delegate的共性即加载根布局，绑定布局等共性操作；

图4.1 BaseDelegate代码

BaseDelegate往下一层，是PermissionCheckerDelegate，也是一个抽象类，抽象提取了作为一个Delegate需要的动态申请权限的所有操作，也就是这一层封装了Delegate有关于权限申请的申请逻辑等；

图4.2 PermissionCheckerDelegate代码

PermissionCheckerDelegate再往下一层，是XiaoYunDelegate，同样是一个抽象类，可以在这一层封装一些最顶层的业务操作、控制功能，此后所有具体的Delegate可以继承自这个XiaoYunDelegate：

图4.3 XiaoYunDelegate代码
如此分层设计，即可以把所有Delegate的必备的共性抽象提取出来，让一个Delegate一旦继承自XiaoYunDelegate并具体实现的时候，便可以马上继承所有上层抽象提取好的方法逻辑，而不用考虑其中的细节，只管调用即可，如BaseDelegate封装了加载布局、绑定布局的细节，PermissionCheckerDelegate封装了申请权限的细节等，子类都无需考虑，只管调用；
如此分层设计，还有另外一个优势就是大幅度地降低耦合性，是各个类的责任职务更加明确，各司其职，井井有条，可读性、可拓展性更佳！

<br>
4.3 全局配置模块封装
原理：Android系统提供了一个Application类，只要准备好一个Application子类，并在AndroidManifest中指定好这个类，这个类的配置代码就可以根据声明周期覆盖整个APP：

图4.4 Application子类代码

图4.5 AndroidManifest代码

根据以上原理，这里可以这样处理，首先，准备一个Configurator类，类中准备一个静态的HashMap，存储所有全局使用的元素，

图4.6 Configurator代码
像任何地方都可能用到的主线程Handler、全局Application上下文等等这些全局都可能使用到的元素，我们就没必要每次要使用的时候再去new它一个实例出来，这样很不优雅，也会再关键时刻消耗时间和性能，所以我们不如未雨绸缪，把他们一开始就准备好，配置在这个静态HashMap中，这样在需要使用到这些实例的时候我们直接从这个HashMap中取得实例即可，就不用去每个地方都new一下了，即逻辑清晰，也节约了性能的支出！


图4.7 使用到的XiaoYun_CONFIGS（即存储配置信息的静态HashMap实例）地方


图4.8 Configurator使用了单例模式


图4.9 使用了建造者模式

第二种，如上，就是我们可以把所有的全局各种地方的需要配置的东西，统一集成到这个Configurator类中来，像字体图标库的配置、网络框架Retrofit需要的host格式配置、WebView需要的事件配置等等，使用建造者模式（方便配置HashMap内容，并且使用连缀调用的思想，使用起来逻辑简洁清晰）的设计思想封装了对应的配置方法在Configurator类中，然后在XiaoYun类中做了二次继承：



图4.10 在XiaoYun类中做了二次继承

图4.11 统一在Application子类中调用并且配置

最后统一在Application子类中调用并且配置，如此一来，我们把整个项目所有的需要配置的东西，封装在了Configurator中，在XiaoYun中直接调用Configurator方法，做了二次封装，最后在Application子类中直接调用XiaoYun类的二次封装方法，采用连缀调用的方式，即可完成对全局API以及各种配置信息的配置，使配置逻辑不再“随用随写”、散落各处（导致逻辑凌乱），而是统一在一个地方进行管理，统筹管理，很大程度地增加了项目的可读性、可拓展性！


<br>
4.4 网络模块封装
项目的网络模块二次封装了现行最流行、使用最广泛的两种网络第三方框架，第一种是Retrofit，其功能较为齐全，架构耦合性低，适用于普遍的场景以及实际场景；第二种是OkHttp，同样可以完成基本的网络操作，但它的使用逻辑没有Retrofit，可以用于简单的网络请求或者上线前的功能测试；
首先Retrofit，RestService用来定义网络请求的相关接口方法：


图4.12 RestService部分代码

RestCreator采用了静态工厂模式封装了Retrofit的初始化以及创建的过程逻辑，以及准备一个静态HashMap用来存储网络请求中需要的提交的数据：


图4.13 RestCreator部分代码

RestClientBuilder采用了建造者模式以及配置方法连缀编程思想，用于构造RestClient实例：


图4.14 RestClientBuilder部分代码

RestClient用于获取Retrofit实例以及RestClientBuilder传递过来的各种配置参数，调用RestService定义的网络接口方法，进行网络请求：


图4.15 RestClient部分代码

如此一来，我们将Retrofit使用过程和职能解耦成四个类文件进行编写，各司其职，并且结合建造者模式、静态工厂模式，使得架构逻辑清晰，具备优秀的可读性和可拓展性，如下是使用案例，只需要一个连缀调用便可以完成各种网络请求：


图4.16 Retrofit二次封装架构的使用案例

<br>
4.5 卡顿优化方案集成
APP卡顿性能优化一直是APP优化的首要目标，APP的卡顿很大程度地影响了APP的用户体验和进程运行性能，本项目中集成了StrictMode卡顿优化方案，即严苛模式，它是Android提供的一种运行时检测机制；如果在开发阶段对成千上万行的代码进行code review，可能效率是比较低下的；使用StrictMode之后，系统会自动检测出来主线程当中违例的一些情况，同时按照代码的配置给出相应的反应，由此我们可以锁定问题所在，进行分析优化，解决卡顿问题。
StrictMode方案主要有线程检测策略以及虚拟机策略等两种问题检测策略：
线程检测策略，StrictMode.setThreadPolicy()：包括，自定义的耗时调用检测，detectCustomSlowCalls()；磁盘读取操作检测，detectDiskReads()；网络操作检测，detectNetwork()等API；
虚拟机策略，StrictMode.setVmPolicy()：包括Activity泄漏检测，detectActivityLeaks()；SqlLite对象泄漏检测，detectLeaked -SqlLiteObjects()；限制实例数量检测，setClassInstanceLimit(要限制的类实例，限制的数量)；
我们将策略封装在ExampleApp，即Application的子类中：

图4.17 项目中的StrictMode集成代码

如果运行时候出现卡顿问题，可以通过Android Studio的logcat中捕获锁定详细的问题堆栈信息：

图4.18 StrictMode测试案例

或者也可以通过观察测试机的运行情况作出判断，即运行时出现卡顿问题时候，可以在运行终端处弹出一个弹框进行警告：

图4.19 StrictMode测试案例

<br>
4.6 二维码封装
　　首先在项目中通过“implementation 'me.dm7.barcodescanner:zb
-ar:1.9.3'”继承二维码框架；
接着继承框架提供的工具类，实现扫描用的自定义View（ScannerView）：
最后在ScannerDelegate中实现具体的触发逻辑即可：


图4.20 二维码扫描

图4.20 二维码扫描测试（通过Toast弹出二维码内涵信息）


<br>
4.7 启动图模块
在LauncherDelegate中，使用SharePreference存储方式存储一个布尔值（键名为ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name()），APP启动时会判断这个布尔值，如果为空说明轮播子模块未曾被启动过，即APP是安装后第一次被打开的，那这时候则启动轮播子模块，如果不为空，则轮播子模块被启动过，那这时候则直接启动倒计时启动模块。
4.7.1 启动图轮播子模块
主要实现逻辑类是LauncherScrollDelegate，UI的逻辑实现上主要是使用了ConvenientBanner框架，用于实现滚动图效果，另外本模块执行完毕后，会把ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name()键的键值设置为true，下次启动APP不在进入轮播子模块，直接启动倒计时启动模块。

图4.20 启动图轮播子模块页面
4.7.2 启动图倒计时子模块
主要逻辑实现类是LauncherDelegate，UI可以提前配置好背景图以及倒计时背景组件，倒计时逻辑是使用Timer类结合TimerTask机制，在子线中开辟一个定时任务，每一秒运行一次，完成倒计时：

图4.21 启动图倒计时子模块页面

<br>
4.8 主页商品展示模块
4.8.1 商品UI展示相关模块
UI展示使用了MVVM架构，IndexDataConverter负责数据处理，即Model层，MultipleRecyclerAdapter服务UI加载以及对应的数据绑定，为ViewModel层，IndexDelegate负责调度以及控制，为View层；使用这个设计模式，有利于代码的解耦以及拓展。
另外，通过准备好各种布局xml文件以及绑定对应的枚举键，以键值对的方式加入到MultipleRecyclerAdapter的ItemType内置数组中，这样一来，MultipleRecyclerAdapter在加载每一个子Item的时候，会根据Item自身数据Bean中的ItemType来选择不同的布局进行加载，即可以根据不同的Item数据来实现不同的准备好的布局加载方式，实现一个RecyclerView完成多种子布局的效果！
同时APP代码模板化，当需要添加新的Item以及为新的Item指定某一个布局的时候，不再需要更改APP端代码，只需要更改云端的数据即可，如更改云端的某一个Item数据中的ItemType值，即可以简介改动APP页面的Item布局UI，实现代码的前后端解耦、灵活性、复用性等。

图4.22 主页商品多布局展示

<br>
4.8.2 商品详情
顶部轮播图使用ConvenientBanner框架实现，只要准备好指标器样式以及图片，同时配置好相关的轮播信息即可实现；顶部轮播图于标题栏使用折叠标题栏组件实现可折叠效果；下方使用TabLayout组件，实现分页展示商品详细信息。

图4.23 商品详情

<br>
4.8.3 上拉加载与下拉刷新
上拉加载和下拉刷新的逻辑，RecyclerView已经准备好了回调方法，项目主要将这两个功能封装在RefreshHandler类中，其中onRefresh()回调方法中完成下拉刷新的加载逻辑，onLoadMoreRequested() 回调方法中完成上拉加载更多的加载逻辑。

<br>
4.9 分类页商品展示模块
分类页商品展示页面主要分两个部分，即基于根界面SortDelegate的基础，再嵌套加载两个子界面，左侧垂直类别列表分界面VerticalListDelegate以及右侧对应类别商品展示分界面ContentDelegate；
左侧VerticalListDelegate同样是以RecyclerView为基础，使用MVVM设计模式实现UI，类似于4.8.1中提到的主页商品布局展示，只不过这里的Item布局比较单一，同时数据也是来自于云端，页面加载时候向云端请求JSON数据，随后依据云端返回的数据渲染UI；另外，每一个类别都有对应的类别id，点击左侧VerticalListDelegate的类别按钮后，按钮会使用这个类别id创建出一个对应的ContentDelegate实例，并将之加载到右侧，触发右侧界面的切换响应；
右侧ContentDelegate则在onCreate()生命周期方法中，拿到左侧传递过来的类别id，并在onBindView()UI加载方法中，封装这个类别id到url，向云端申请对应类别的商品展示数据，拿到数据后完成商品展示：

图4.24 分类页商品展示


<br>
4.10 发现页用户卡片模块
<br>
4.10.1 发现页用户卡片UI实现
本页面使用的WebView（Android原生）与H5交互的技术，即本地或者远程端准备好H5项目包（HTML、CSS、JavaScript等相关网页文件），而本地利用WebView技术，将网页渲染成具备APP特性的页面，同时H5端可以配置H5端的事件，WebView原生端可以实现自己原生端的事件，两者相互解耦，又可以相互调用，实现了灵活的布局、任务分配及设计模式：

图4.25 发现页

<br>
4.10.2 一键分享集成
在微信开放平台上，把自己的项目应用注册上去，获得微信开发平台的应用key；同时在ModSDK的官方平台上，同样把自己的项目应用注册上去，使得应用获得集成ModSDK的资格，接着在项目中集成ModSDK，并将项目应用在微信开发平台获得的应用key以及ModSDK获得应用key配置到项目中进行初始化即可；
随后在ShareEvent中编写分享的内容以及效果，在H5端的JavaScript代码中（index.js）接入原生端的ShareEvent事件内容，同时配置分享的各个信息，即可完成一键分享：

图4.26 一键分享到微信

<br>
4.11 购物车商品列表展示模块
<br>
4.11.1 页面UI与数据的整体控制、同步
购物车页面根界面主要是基于ShopCartDelegate文件进行设计的，其中onLazyInitView()中负责网络数据的请求，更新和加载；onClickSelectAll()回调方法的内容则完成了全选按钮的触发逻辑，onClickRemoveSelectedItem()回调方法的内容则完成了删除按钮的相关逻辑， onClickClear()回调方法的内容则完成了清空按钮的相关逻辑；checkItemCount()方法中的逻辑则用于判断页面中的订单Item数据，若没有数据则加载组件ViewStub，显示对应的UI界面；

<br>
4.11.2 购物车商品Item
这里商品Item显示同样使用了上述的MVVM设计模式进行对应的设计，ShopCartDataConverter负责Model层的数据分析与处理，ShopCartAdapter则负责ViewModel层的实现，即UI与数据的绑定和处理；ShopCartAdapter中包括对每一个Item的UI与数据的绑定和加载、Item中商品加减按钮的逻辑实现和数据同步逻辑设计等等；

图4.27 购物车页面

<br>
4.12 “我的”用户个人信息模块
“我的”用户个人信息页面整体根界面在PersonalDelegate文件中进行设计，在这个文件中实现了个人信息界面的所有功能的基本图标展示以及所有功能图标的点击触发事件，触发后可以进入其他的功能子模块：

图4.28 “我的”个人信息页面

<br>
4.12.1 个人信息修改模块
个人信息修改模块的逻辑实现主要是在UserProfileDelegate文件中，其中头像修改模块集成了拍照取图、本地相册取图的功能；姓名修改模块则由文本按钮组件组实现；性别修改模块由弹框及单选按钮组实现；生日由日历组件弹框实现：

图4.29 “我的”个人信息修改页面

<br>
4.12.2 订单展示模块
订单展示界面主要由OrderListDelegate进行逻辑实现，主要是请求云端数据随后进行UI订单展示；
每一个订单都可以被点击随后进入订单评价界面，订单评价界面主要逻辑在文件OrderCommentDelegate中设计，五星评价组件由自定义的组件StarLayout实现，上传商品图片由自定义的组件AutoPhotoLayout实现：

图4.30 订单展示模块（左）、订单评价页面（右）

<br>
4.12.3 地址管理模块
地址管理模块主要逻辑在AddressDelegate文件中实现，其中页面的加载以及网络数据的请求与同步在onBindView()方法中完成，同时完成删除卡片以及添加数据的逻辑触发：

图4.31 地址管理页面

<br>
4.12.4 系统设置模块
系统设置页主要在SettingsDelegate文件中实现，同时实现按钮的点击触发，如点击“关于”按钮后，可以跳转到“关于”界面，“关于”界面由AboutDelegate文件实现逻辑，主要对APP项目的简介内容进行展示：

图4.32 系统设置界面（左）、“关于”界面（右）
