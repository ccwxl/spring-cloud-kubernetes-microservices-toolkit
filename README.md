### k8s 调试工具

k8s 环境下开发调试 spring cloud 微服务

## 功能

- k8s作为注册中心
- 使用`apisix`作为代理和`apix-gateway`. 本地服务直接调用的k8s内的服务
- 使用`apisix`的`java`插件实现协调和服务发现
- 保证测试环境的公共服务不要调用到本地服务.服务调用隔离

## config ui(待实现)

- 实现一个k8s的configmap的ui. 支持微服务的配置中心
- webjar
- Vue+Vite
- redis 存储历史版本

## Notice

- https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-http-interface 使用替换feign
- 支持native.

## 说明.

- 如[flow](req-flow.puml) 所示的请求流程.
- 坑: k8s 的service的`sessionAffinity`要为`Local`不然获取不到client的ip
- 坑: apisix 的upstream只有一个节点时不走具体的负载均衡插件.
- 坑: apisix 的路由匹配规则需要为. 以区分代理和正常的路由请求

```yaml
apisix:
  router:
    http: 'radixtree_host_uri'
```