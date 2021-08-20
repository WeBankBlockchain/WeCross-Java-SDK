### v1.2.0

(2021-08-20)

**新增**

* 适配Fabric2.0类型的链账户

### v1.1.1

(2021-04-02)

**更改**

* 支持调用Router的URL前缀可配

### v1.1.0

(2020-02-02)

**更改**

* 升级`spring-boot-xxx`版本，详情参考`build.gradle`修改内容
* 修改`login` `register`接口，添加`token`认证流程，简化使用方式

### v1.0.0

(2020-12-17)

**新增**

* 配置新增SSL开关项
* 新增事务注解：@Transactional、 @Path
* 新增事务接口：invoke，根据上下文自动选择发交易的方式
* 新增账户相关接口：register、login、logout、addChainAccount、setDefaultAccount、setDefaultAccount

**更改**

* 资源调用相关接口不再需要传入账户名
* 压测程序适配v1.0.0的压测方式
* 接口重命名: 
    * getTransactionInfo => getXATransaction
    * getTransactionIDs => listXATransactions
    * startTransaction => startXATransaction
    * commitTransaction => commitXATransaction
    * rollbackTransaction => rollbackXATransaction
    * execTransaction => sendXATransaction
    * callTransaction => callXA

### v1.0.0-rc4

(2020-08-18)

**新增**

* 新增调用router的customCommand接口
* 新增调用router的2PC事务接口
  * startTransaction、execTransaction、callTransaction、commitTransaction、rollbackTransaction
  * getTransactionInfo、getTransactionIDs
* 新增2PC的压测程序

**更改**

* 压测程序适配v1.0.0-rc4的压测方式

### v1.0.0-rc3

(2020-06-16)

**新增**

* 新增异步请求API
* 新增多种场景的压测程序

**更改**

* 将spring-boot async client更换为基于netty的async-http-client

### v1.0.0-rc2

(2020-05-12)

**新增**

* 安全通讯：SDK和Router之间采用TLS协议通讯
* 资源接口：提供资源访问相关接口
* 配置文件：新增配置文件，配置TLS证书信息

**更改**

* RPC接口调整
- 合约调用需要指定签名的账户
- 合约调用不再需要指定返回值类型列表，因此删除了相关衍生接口

### v1.0.0-rc1

(2019-12-30)

**功能**

* 提供基础跨连操作接口

**兼容**

* WeCross通信协议版本v1，适配WeCross跨连路由v1.0.0-rc1版本

