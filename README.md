# Crypto Exchange 微服务项目

一个基于 **Spring Boot 3.x + Spring Cloud 2023 + Nacos + Gateway + Redis + JWT**  
的 **微服务交易系统基础架构**。

当前阶段已完成：
- 微服务注册与发现
- Gateway 路由转发
- 统一鉴权（Gateway 注入用户上下文）
- 用户服务基础校验
- Redis / Token / Auth 模块拆分完成

---

##  一、项目整体架构
crypto-exchange
├── gateway-service                # 网关服务（WebFlux）
├── user-service                   # 用户服务（Web MVC）
├── account-service                # 账户服务（待扩展）
├── order-service                  # 订单服务（待扩展）
├── settlement-service             # 清算服务（待扩展）
│
├── common-core                    # 核心工具（常量 / R / 异常）
├── common-web                     # Web 通用（Controller Advice / 返回结构）
├── common-redis                   # Redis / Redisson 统一封装
│
├── common-auth
│   ├── common-auth-core           # 鉴权核心（Token / Context / 注解定义）
│   ├── common-auth-web            # Servlet 鉴权（预留）
│   └── common-auth-webflux        # Gateway 鉴权（GlobalFilter）
│
└── pom.xml                        # 父 POM
