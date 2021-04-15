![](docs/images/menu_logo_wecross.png)

# WeCross-Java-SDK

[![CodeFactor](https://www.codefactor.io/repository/github/webankblockchain/wecross-Java-SDK/badge)](https://www.codefactor.io/repository/github/webankblockchain/wecross-Java-SDK) [![Build Status](https://travis-ci.org/WeBankBlockchain/WeCross-Java-SDK.svg?branch=master)](https://travis-ci.org/WeBankBlockchain/WeCross-Java-SDK) [![codecov](https://codecov.io/gh/WeBankBlockchain/WeCross-Java-SDK/branch/dev/graph/badge.svg)](https://codecov.io/gh/WeBankBlockchain/WeCross-Java-SDK) [![Latest release](https://img.shields.io/github/release/WeBankBlockchain/WeCross-Java-SDK.svg)](https://github.com/WeBankFnTech/WeCross-Java-SDK/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/com.webank/wecross-java-sdk)](https://search.maven.org/artifact/com.webank/wecross-java-sdk) [![License](https://img.shields.io/github/license/WeBankBlockchain/WeCross-Java-SDK)](https://www.apache.org/licenses/LICENSE-2.0) [![Language](https://img.shields.io/badge/Language-Java-blue.svg)](https://www.java.com)

WeCross Java SDK提供操作跨链资源的Java API，开发者通过SDK可以方便快捷地基于[WeCross](https://github.com/WeBankBlockchain/WeCross)开发自己的跨链应用。

## 关键特性

- 提供调用WeCross[RPC接口](https://wecross.readthedocs.io/zh_CN/latest/docs/manual/api.html)的Java API
- 封装了跨链资源操作接口

## 使用SDK

gradle

```
compile ('com.webank:wecross-java-sdk:1.1.1')
```

maven

```
<dependency>
    <groupId>com.webank</groupId>
    <artifactId>wecross-java-sdk</artifactId>
    <version>1.1.1</version>
</dependency>
```

更多配置及使用教程请参照[SDK技术文档](https://wecross.readthedocs.io/zh_CN/latest/docs/dev/sdk.html)

## 源码编译

**环境要求**:

  - [JDK8及以上](https://www.oracle.com/java/technologies/javase-downloads.html)
  - Gradle 5.0及以上

**编译命令**:

```shell
$ cd WeCross-Java-SDK
$ ./gradlew assemble
```

如果编译成功，将在当前目录的dist/apps目录下生成sdk jar包。

## 贡献说明

欢迎参与WeCross社区的维护和建设：

- 提交代码(Pull requests)，可参考[代码贡献流程](CONTRIBUTING.md)以及[wiki指南](https://github.com/WeBankBlockchain/WeCross/wiki/%E8%B4%A1%E7%8C%AE%E4%BB%A3%E7%A0%81)
- [提问和提交BUG](https://github.com/WeBankBlockchain/WeCross-Java-SDK/issues/new)

希望在您的参与下，WeCross会越来越好！

## 社区
联系我们：wecross@webank.com

## License

WeCross Java SDK的开源协议为Apache License 2.0，详情参考[LICENSE](./LICENSE)。