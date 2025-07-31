云报全媒体绩效考核客户端项目架构总览
==============================

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

# 三方组件

- FlowBus 协程流shareFlow处理事件总线，替代eventbus

# 自定义组件

- YBToast 自定义SnackBar仿Toast效果，多次show以最新的一次内容显示
使用方法：snackBarHostState.showToast(message)

- YBImage 支持加载网络和本地图片，网络加载引用的Coil

==============================================