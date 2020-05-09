## 此仓库不再更新，新仓库地址：  https://gitee.com/code-together/my_curd
## my_curd 

不够漂亮高大上，足够轻量清纯，分分钟掌控不是事，为所欲为。

## 简介

1. 菜单级、按钮级权限控制。 自定义注解 + 拦截器 + freemarker宏标签 设计，清晰明确，容易掌控。 
2. 代码生成器。 单表、主从表，增删改查导入导出 功能一键生成，拦截器动态生成sql where条件。
3. 多配色风格。保持 easyui 方便易用的前提下，美化风格，数据型应用非常合适。
4. 系统推送通知。  websocket主动，分角色推送通知，通知存库，未读已读 。
5. 整合activiti 实现 业务表数据 流程审批 。业务表 结合代码生成器编写少量代码，审批流已测支持 （用户任务 + 排他网关 + 监听器 + 申请表单调整）简单流转功能。  

## 部署 
   
1. 安装 jdk8(+)，maven，mysql5.7 数据库。
2. 创建数据库 my_curd, 将 db/my_curd.sql 导入； 创建数据库 my_curd_oa,将 my_curd_oa.sql 导入，
修改 resoureces/config-dev.txt 中 相关 数据源连接信息。
3. 项目根目录下 mvn clean package, 进入 target/my_curd-release/my_curd, 使用 start.bat 或 start.sh 启动项目
4. 访问 http://localhost/dashboard, 账号 admin, 密码 123456 或 111111
5. 更多 测试账号 进入系统查看，默认密码为 123456. 

## 技术方案   
 
- JFinal4.7
- Easyui1.8.5
- Activiti5.22 
- Mysql5.7

## 截图    

#### 权限管理  
![菜单管理](https://s2.ax1x.com/2019/11/08/MEcEkV.png) 
![角色管理](https://s2.ax1x.com/2019/11/08/MEcnl4.png) 
![用户管理](https://s2.ax1x.com/2019/11/08/MEcl01.png)

#### 代码生成器   
![代码生成器](https://s2.ax1x.com/2019/11/08/MEcFwq.png)
![生成1对多增删改查](https://s2.ax1x.com/2019/11/08/MEW7Z9.png)

####  流程 
![流程申请](https://s2.ax1x.com/2019/11/08/MEcu6J.png) 
![流程办理](https://s2.ax1x.com/2019/11/08/MEcZfU.png) 
![候选任务](https://s2.ax1x.com/2019/11/08/MEcVYT.png) 
![已办流程](https://s2.ax1x.com/2019/11/08/MEcKX9.png) 

## License

[Apache 2.0](https://github.com/qinyou/my_curd/blob/master/LICENSE)

Copyright (c) 2017-present qinyou
