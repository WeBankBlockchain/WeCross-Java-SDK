### v1.0.0-rc4

(2020-08-18)

**新增**

* 新增调用router的customCommand接口
* 新增调用router的2PC事务接口
  * startTransaction、execTransaction、commitTransaction、rollbackTransaction
  * getTransactionInfo、getTransactionIDs
* 新增2PC的压测程序

**更新**

* 压测程序适配 v1.0.0-rc4的压测方式

### v1.0.0-rc3

(2020-06-16)

**新增**

* 新增异步请求API
* 新增多种场景的压测程序

**更新**

* 将spring-boot async client更换为基于netty的async-http-client

### v1.0.0-rc2

(2020-05-12)

**新增**

* 安全通讯：SDK和Router之间采用TLS协议通讯
* 资源接口：提供资源访问相关接口
* 配置文件：新增配置文件，配置TLS证书信息

**更新**

* RPC接口调整
- 合约调用需要指定签名的账户
- 合约调用不再需要指定返回值类型列表，因此删除了相关衍生接口

### v1.0.0-rc1

(2019-12-30)

**功能**

* 提供基础跨连操作接口

**兼容**

* WeCross通信协议版本v1，适配WeCross跨连路由v1.0.0-rc1版本

