云报全媒体绩效考核客户端项目架构总览
====================================

# app

主壳工程，整合APP正常运行所需要的模块，包含UI框架和应用级导航。

# core

核心通用库模块，可以依赖于其它的core。

- common 公共资源，工具类等
- data 数据层相关，数据模型等
- network 网络层相关
- widget UI组件，公共轮子

# feature

核心功能层模块，feature彼此之间隔离，可以依赖底层的core。

- basis 基础功能模块，如登录、注册、我的等页面功能
- review-data 绩效考核数据模块
- review-manager 绩效考核管理模块

# build-logic

自定义插件模块，用于Gradle构建逻辑，三方组件的快速集成。

==============================================

# 官方组件

- hilt 依赖注入框架
- retrofit 网络框架
- navigation 路由框架
- coil 图片加载

# 主题

- YBShape 形状定义，主要涉及圆角等
  - 使用方法：YBShape.X 

# 三方组件

- FlowBus 协程流shareFlow处理事件总线，替代eventbus

# 自定义组件

- YBToast 自定义SnackBar仿Toast效果，多次show以最新的一次内容显示
  使用方法：snackBarHostState.showToast(message)
- YBImage 支持加载网络和本地图片，网络加载引用的Coil
- YBPopup:自定义的底部弹出窗口
  - 使用方法：可在任意层级添加，通过传入的visible控制显示和隐藏。
- YBPicker 基于popup实现，是一个基础的选择器组件，支持多列选择。
- SingleColumnPicker 单列的选择器，基于YBPicker实现。
- YBTimePicker 是一个时间选择器组件，基于YBPicker实现。
- YBDatePicker 是一个日期选择器组件，基于YBPicker实现。
- YBBadge 红点组件，有小红点和带数字的红点两种类型，可以自定义颜色。
  - 使用方法：将需要显示红点的组件与Box组合，可在YBBadge中传入Aliment控制位置。
- YBDialog 自定义通用弹窗。
- YBAutoDismissDialog 自动消失的状态提示弹窗，如成功、失败等。
- YBLoadingDialog 加载中弹窗

## 下拉菜单
详见DropdownMenu.kt
- YBDropdownMenu 定义与页面等宽的下拉菜单，item项文字居中显示（一般用于页面顶部选择）
- YBAlignDropdownMenu 定义自适应item宽度或与anchor组件等宽的下拉菜单，item项文字居左显示（一般用于页面内筛选项）



==============================================

