![](docs/images/menu_logo_wecross.svg)

# WeCross-Java-SDK
[![CodeFactor](https://www.codefactor.io/repository/github/webankfintech/wecross-Java-SDK/badge)](https://www.codefactor.io/repository/github/webankfintech/wecross-Java-SDK) [![Build Status](https://travis-ci.org/WeBankFinTech/WeCross-Java-SDK.svg?branch=master)](https://travis-ci.org/WeBankFinTech/WeCross-Java-SDK) [![codecov](https://codecov.io/gh/WeBankFinTech/WeCross-Java-SDK/branch/dev/graph/badge.svg)](https://codecov.io/gh/WeBankFinTech/WeCross-Java-SDK) [![Latest release](https://img.shields.io/github/release/WeBankFinTech/WeCross-Java-SDK.svg)](https://github.com/WeBankFnTech/WeCross-Java-SDK/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/com.webank/wecross-java-sdk)](https://search.maven.org/artifact/com.webank/wecross-java-sdk) ![](https://img.shields.io/github/license/WeBankFinTech/WeCross-Java-SDK) 

WeCross-Java-SDK为[WeCross](https://github.com/WeBankFinTech/WeCross)提供Java API。开发者通过SDK可以方便快捷地基于WeCross开发自己的跨链应用。

## 关键特性

- 实现WeCross的[Resetful-RPC](https://wecross.readthedocs.io/zh_CN/latest/docs/manual/api.html)的Java API。

## 使用SDK

gradle

```
compile ('com.webank:wecross-java-sdk:1.0.0-rc1')
```

maven

```
<dependency>
    <groupId>com.webank</groupId>
    <artifactId>wecross-java-sdk</artifactId>
    <version>1.0.0-rc1</version>
</dependency>
```

## 源码编译

**环境要求**:

  - [JDK8及以上](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/sdk.html#id1)
  - Gradle 5.0及以上

**编译运行如下命令**:

```shell
$ cd WeCross-Java-SDK
$ ./gradlew assemble
```

**编译结果**: 
编译的WeCross-Java-SDK jar位于:

```shell
WeCross-Java-SDK/dist/apps/wecross-java-sdk.jar
```

## 贡献说明

欢迎参与WeCross社区的维护和建设：

- 如项目对您有帮助，欢迎点亮我们的小星星(点击项目左上方Star按钮)。
- 提交代码(Pull requests)，参考我们的[代码贡献流程](CONTRIBUTING_CN.md)。
- [提问和提交BUG](https://github.com/WeBankFinTech/WeCross-Java-SDK/issues/new)。
- 如果发现代码存在安全漏洞，请在[这里](https://security.webank.com)上报。

希望在您的参与下，WeCross会越来越好！

## 社区
联系我们：wecross@webank.com

## License

![license](http://img.shields.io/badge/license-Apache%20v2-blue.svg)

Web3SDK的开源协议为[Apache License 2.0](http://www.apache.org/licenses/). 详情参考[LICENSE](./LICENSE)。