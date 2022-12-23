## 启动报错

- `Caused by: org.xml.sax.SAXParseException: 外部 DTD: 无法读取外部 DTD 'mybatis-3-mapper.dtd', 因为 accessExternalDTD
  属性设置的限制导致不允许 'https' 访问`
- 则增加一下参数

```
./example-aot-native-mybatis-plus -Djavax.xml.accessExternalDTD=all
```