## 调用链路

`开发者本地`--->`apisix-gateway`-->`service-account`-->`service-uaa`-->`organization-system`

## 实现

k8s作为注册中心
可以通过`apisix`直接调用的本地服务
如果本地服务启动.服务间调用优先调用本地服务
保证测试环境的公共服务不要调用到本地服务

## config ui
