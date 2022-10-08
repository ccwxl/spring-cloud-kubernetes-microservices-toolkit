### k8s 调试工具

k8s 环境下开发调试 spring cloud 微服务

## 功能

- k8s作为注册中心
- 使用`apisix`作为代理. 本地服务直接调用的k8s内的服务
- 使用`apisix`的`java`插件实现协调和服务发现
- 保证测试环境的公共服务不要调用到本地服务.服务调用隔离

## config ui

- 实现一个k8s的configmap的ui. 支持微服务的配置中心