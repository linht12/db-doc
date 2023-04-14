# 数据库文档生成工具

通过读取数据库的表字段，生成excel和word等数据文档

使用方法
----
    1.项目拉取下来，直接运行项目
    2.登录http://lcoalhost:8080/h2，其中登录账号密码默认是root/root,连接信息是：jdbc:h2:./data/h2
    3.把./data目录下的schema-h2.sql放在控制台执行，创建表
    4.登录swagger：http://127.0.0.1:8080/swagger-ui/index.html，进行添加数据库信息，然后再导出数据库文档
    5.通过db-controller里的/db/add接口增加数据库信息后得到id。然后在/export/word或/export/excel传入id导出数据库文档

使用的框架
----
    1.spring boot 3.0.5
    2.h2数据库
    3.springdoc
    4. jdk17
    5.freemaker



支持数据库
----
1.  mysql



#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
