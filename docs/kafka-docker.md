# 本地 Kafka（Docker）

本项目通过独立的 `docker-compose.kafka.yml` 启动 Kafka。与 MySQL / Eureka / ems-backend 等可并行运行。

**购物后会员积分**已改为 Kafka 异步：`ems-backend` 发布 → `membership-service` 消费（topic：`membership.purchase.points`）。

## 前置条件

1. 安装并启动 [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/)（建议启用 WSL2）。
2. 确认命令可用：

```powershell
docker --version
docker compose version
```

## 端口

| 服务 | 端口 | 说明 |
|------|------|------|
| Kafka | 9092 | 本机客户端 `bootstrap-servers=localhost:9092` |
| Kafka UI | 8085 | 浏览器 http://localhost:8085 |

与项目其它服务（8080 ems-backend、8081 membership、8761 Eureka 等）无冲突。

## 启动与停止

在项目根目录 `easyfullstack` 下执行：

```powershell
# 启动（后台）
docker compose -f docker-compose.kafka.yml up -d

# 查看状态
docker compose -f docker-compose.kafka.yml ps

# 查看日志
docker compose -f docker-compose.kafka.yml logs -f kafka

# 停止
docker compose -f docker-compose.kafka.yml down

# 停止并删除数据（清空所有 topic）
docker compose -f docker-compose.kafka.yml down -v
```

## 验证 Kafka

### 方式 A：容器内命令行

```powershell
# 创建 topic
docker compose -f docker-compose.kafka.yml exec kafka /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --create --topic test --partitions 1 --replication-factor 1

# 列出 topic
docker compose -f docker-compose.kafka.yml exec kafka /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 --list

# 生产消息（输入几行后 Ctrl+C 退出）
docker compose -f docker-compose.kafka.yml exec -it kafka /opt/kafka/bin/kafka-console-producer.sh `
  --bootstrap-server localhost:9092 --topic test

# 消费消息（应能看到刚才输入的内容，Ctrl+C 退出）
docker compose -f docker-compose.kafka.yml exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh `
  --bootstrap-server localhost:9092 --topic test --from-beginning
```

### 方式 B：Kafka UI

1. 打开 http://localhost:8085
2. 左侧选择预置 cluster **`local`**（bootstrap 为 `kafka:9092`，见 [`docker-compose.kafka.yml`](../docker-compose.kafka.yml)）
3. 在 **Topics** 中查看、浏览消息（购物相关 topic：`membership.purchase.points`）

**若 UI 显示 cluster Offline（最常见原因）**

| 错误做法 | 正确做法 |
|----------|----------|
| 新建 cluster，bootstrap 填 `localhost:9092` | 使用已有 **`local`**，或新建时填 **`kafka:9092`** |
| 不重启 UI 就反复刷新 | 先确认 `docker compose ps` 中 kafka 为 **healthy**，再执行 `docker compose -f docker-compose.kafka.yml restart kafka-ui` |

说明：Kafka UI 运行在 Docker 网络内，`localhost:9092` 指向 UI 容器自身，**连不到** Kafka broker。本机 Spring 应用（ems-backend、membership-service）在宿主机运行，应使用 **`localhost:9092`**。

### 一键冒烟脚本（PowerShell）

```powershell
docker compose -f docker-compose.kafka.yml exec kafka /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --create --if-not-exists --topic test --partitions 1 --replication-factor 1

echo "hello-kafka" | docker compose -f docker-compose.kafka.yml exec -T kafka `
  /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test

docker compose -f docker-compose.kafka.yml exec kafka /opt/kafka/bin/kafka-console-consumer.sh `
  --bootstrap-server localhost:9092 --topic test --from-beginning --max-messages 1 --timeout-ms 10000
```

## 与业务服务联调（购物积分）

### 推荐启动顺序

1. MySQL、Redis（若使用）
2. `docker compose -f docker-compose.kafka.yml up -d`
3. Eureka（8761）
4. **membership-service**（8081，Kafka 消费者，需先起以便消费消息）
5. **ems-backend**（8080，Kafka 生产者）
6. api-gateway（9000）、前端（3000）

### Topic 与消息

| 项 | 值 |
|----|-----|
| Topic | `membership.purchase.points` |
| 生产者 | ems-backend（购物事务提交后） |
| 消费者 | membership-service → `addPointsForPurchase` |
| 消息体 | `{ "userId": 1, "totalCost": "99.50" }` |

学生完成购买后，接口立即返回；个人中心里的**会员积分可能延迟数秒**才更新，属正常现象。可在 Kafka UI（http://localhost:8085）查看该 topic 消息。

### Spring 配置（已写入各服务 `application.properties`）

```properties
spring.kafka.bootstrap-servers=localhost:9092
```

ems-backend 与 membership-service 已配置 `spring.json.add.type.headers=false` / `use.type.headers=false`，避免跨模块 DTO 类名导致反序列化失败。membership-service 使用 `ErrorHandlingDeserializer`，单条坏消息不会拖死整个消费者。

本地为明文协议 `PLAINTEXT`，无需 SASL/SSL。

## 常见问题

| 现象 | 处理 |
|------|------|
| `Connection refused` 连 9092 | 确认 Docker 已运行；`docker compose ps` 中 kafka 为 healthy；本机使用 `localhost:9092`，不要用容器名 |
| 端口 9092 被占用 | 修改 compose 中 `ports` 为 `9094:9092`，并设置 `KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9094` |
| kafka-ui 打不开 | 等待 kafka healthcheck 通过后再访问 8085 |
| Kafka UI cluster **Offline** | 使用 cluster `local`（`kafka:9092`），勿在 UI 里填 `localhost:9092`；见上文「方式 B」 |
| UI/CLI **消费了**但会员积分没变 | 只有 **membership-service**（消费者组 `membership-service`）会写积分；UI 浏览消息或临时 consumer **不会**加积分。勿在 UI/CLI 使用 group `membership-service` 手动消费，否则 offset 被提交后 Spring 监听器读不到。以 **membership-service 日志** 是否出现 `Consuming purchase points message` 为准；`totalCost` 须 **> 0**（积分 = totalCost × 10） |
| 内存不足 | Docker Desktop → Settings → Resources，建议至少 4GB |

### 消费了但没加积分（详细）

1. **确认谁在消费**：Kafka UI 的 Messages / Consume 不等于业务消费。积分由 `membership-service` 的 `@KafkaListener` 写入 `membership_db`。
2. **看消费者组 lag**：Kafka UI → Consumer Groups → `membership-service` → topic `membership.purchase.points`，lag 应随购买下降为 0。
3. **看服务日志**：
   - ems-backend：`Published purchase points message` 或 `Failed to publish purchase points message`
   - membership-service：`Consuming purchase points message`；若 `Skipping purchase points` 说明 `totalCost <= 0`
4. **重启服务**：改 Kafka 配置或代码后须重启 **membership-service (8081)** 与 **ems-backend (8080)**。
5. **补积分（开发）**：`POST http://localhost:8081/api/internal/memberships/users/{userId}/points/purchase`，Header `X-Internal-Api-Key: change-me-in-prod`，body `{"totalCost":99.50}`。
6. **畸形消息卡住消费者**：若 topic 里有人用 PowerShell 误发了带反斜杠的 JSON，消费者可能反复报 `SerializationException`。在容器内用正确 JSON 测试，例如：
   ```bash
   docker compose -f docker-compose.kafka.yml exec kafka bash -c "printf '%s\n' '{\"userId\":4,\"totalCost\":5.00}' | /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic membership.purchase.points"
   ```
   必要时在消费者停用时将 group `membership-service` reset 到 `--to-latest`。

## 架构说明

- **KRaft 模式**：单容器同时担任 broker + controller，无需 Zookeeper。
- **数据卷** `kafka-data`：重启容器后 topic 数据保留；`down -v` 会清空。
