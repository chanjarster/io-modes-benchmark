# IO Modes Benchmark

对应[关于同步/异步、阻塞/非阻塞IO的摘要][0]提到的测试。

用[The reactor pattern and non-blocking IO][1]提到的模式来测试不同类型的IO模式：

准备工作：

1. 弄一个目录，里面放个2000个图片文件，名字为`[1-2000].png`
1. 修改代码里的`getFileBasedOnId`方法，改成图片所在目录
1. 下载一个Tomcat
  * 修改server.xml，修改maxThreads、minSpareThreads（我用的是2000）
  * 新建文件`$TOMCAT_HOME/bin/setenv.sh`，内容`CATALINA_OPTS=-Xms1G -Xmx1G`
  * 删掉`$TOMCAT_HOME/webapps`里的所有东西
1. 修改vertx pom.xml中的JVM参数

执行测试Tomcat和vertx不能同时执行：

* servlet，打成war，名字改成ROOT.war，放到Tomcat里
* vertx，运行`mvn exec:exec`执行
* gatling，benchmark脚本，运行`mvn gatling:test`执行，详见[文档][2]。然后到`target/gatling/`看结果。

[0]: https://chanjarster.github.io/post/kernel/sync-async-blocking-non-blocking-io-abstract/
[1]: https://www.celum.com/en/blog/technology/the-reactor-pattern-and-non-blocking-io
[2]: https://gatling.io/docs/current/extensions/maven_plugin/#maven-plugin