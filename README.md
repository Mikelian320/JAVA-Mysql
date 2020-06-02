# JAVA-Mysql
这是一个查询mysql的java项目

## 框架
* spring-boot

## 本地启动
`./mvnw spring-boot:run`

## 打包
`mvn package` 生成 `jar` 包

`java -jar XXX.jar`

## 部署
已使用 `jenkins` 进行部署,见 `Jenkinsfile`，大致步骤如下

1. 打包
1. 上传jar包
1. 根据分支判断端口，并启动服务

## 测试环境/生产环境
不同环境使用相同的代码，只在部署阶段有做区分，部署至不同的端口，见 `restartSpringBoot`