
## my_curd 

超轻量 快速开发 脚手架，没有复杂技术，没有过度封装，没有繁重依赖。  
 :grin:  轻易掌控，为所欲为  :stuck_out_tongue_closed_eyes: 

### 简介

1. 两级权限：菜单、按钮。 自定义注解 + 拦截器 + freemarker标签 设计，清晰易掌控。  
2. 代码生成器： 单表、主从表（一个主表，N个从表），增删改查导入导出 代码一键生成，样板代码不用写。
3. 审批流：基于activiti5实现，申请、待办、候选、已办、表单、模型、部署、定义、实例、用户、消息推送 等 完整封装。
4. 系统通知： websocket消息，分角色推送通知，通知存库 分未读已读 。
5. 大量通用代码封装，开发代码量小。前台多配色，UI简洁易维护。


### 开始
   
1. 安装 jdk8(+)、maven、mysql5.7 。
2. 创建数据库 my_curd, 将 db/my_curd.sql 导入, 修改 resoureces/config-dev.txt 中 数据源连接信息。
3. 项目根目录下 mvn clean package, 进入 target/my_curd-release/my_curd, 使用 start.bat 或 start.sh 启动项目。
4. 访问 http://localhost/dashboard, 账号 admin, 密码 123456 或 111111
5. 更多 测试账号 进入系统查看，默认密码为 123456. 

### 选型
 
- JFinal4.8
- Activiti5.22 
- Mysql5.7
- easyui1.8.5

### 截图  

1. 系统菜单
![系统菜单](https://images.gitee.com/uploads/images/2020/0719/190203_e745eb81_743575.png "3.png")  
2. 代码生成器 生成一对多 增删改查  
![代码生成器 一对多](https://images.gitee.com/uploads/images/2020/0719/190251_9d8f8bed_743575.png "4.png")   
3. 流程审批 
![流程详情](https://images.gitee.com/uploads/images/2020/0719/190342_1e3378e4_743575.png "2.png")  
![流程消息](https://images.gitee.com/uploads/images/2020/0719/190403_608dee0d_743575.png "5.png")  
4. 整合 Activiti Modeler 
![activiti modeler](https://images.gitee.com/uploads/images/2020/0719/190426_d376934b_743575.png "1.png")  

### License

[Apache 2.0](https://github.com/qinyou/my_curd/blob/master/LICENSE)

Copyright (c) 2017-present qinyou
