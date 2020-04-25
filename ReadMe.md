# Kafka初认识
## kafka使用背景
> kafka定义： 是一个分布式消息系统，由LinkedIn使用Scala编写，用作LinkedIn的活动流（Activity Stream）和运营数据处理管道（Pipeline）的基础，具有高水平扩展和高吞吐量。

> 应用领域： 已被多家不同类型的公司作为多种类型的数据管道和消息系统使用。如:淘宝，支付宝，百度，twitter等

> 目前越来越多的开源分布式处理系统如Apache flume、Apache Storm、Spark,elasticsearch都支持与Kafka集成。

- RabbitMQ：Erlang编写，支持多协议AMQP,XMPP,SMTP,STOMP。支持负载均衡、数据持久化。同时支持p2p和发布/订阅模式
- Redis：基于Key-Value对的NoSQL数据库，同时支持MQ功能，可做轻量级队列服务使用。就入队操作而言，Redis对短消息（小于10KB)的性能比
RabbitMQ好，长消息的性能比RabbitMQ差。
- ZeroMQ：轻量级，不需要单独的消息服务器或中间件，应用程序本身扮演该角色，Peer-to-Peer。实质上是一个库，
需要开发人员自己组合多种技术，使用复杂度高。
- ActiveMQ：JMS实现，Peer-to-Peer。支持持久化，XA事务
- Kafka/Jafka：高性能跨语言的分布式发布/订阅消息系统，数据持久化。全分布式，同时支持在线和离线处理
- MetaQ/RocketMQ：纯Java实现，发布/订阅消息系统，支持本地事务和XA分布式事务。

## kafka的安装与使用


1.Docker 上安装zookeeper和kafka
- 安装zookeeper docker build -t lynch/zookeeper:3.4.6  -f kafka.DockerFile .
- 安装kafka  docker build -t lynch/kafka:2.3.1  -f kafka.DockerFile .
- 启动zookeeper docker run -itd --name zookeeper -h zookeeper -p 2181:2181 lynch/zookeeper:3.4.6 bash
- 启动kafka docker run -itd --name kafka -h kafka --link zookeeper -p 9092:9092 lynch/kafka:2.3.1 bash
- 执行kafka容器 docker exec -it kafka bash


2.Kafka的使用
- 创建topic(test1)  bin/kafka-topics.sh --create --topic test1 --zookeeper zookeeper:2181 --partitions 3 --replication-factor 1
- 查看topic的描述  bin/kafka-topics.sh --describe  --topic test1 --zookeeper zookeeper:2181
- 查看全部topic  bin/kafka-topics.sh --list --zookeeper zookeeper:2181
- 创建消费者 bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test1 --from-beginning
- 创建生产者 bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test1



Options |	Mean
—  |  —
-i	| 以交互模式运行容器，通常与 -t 同时使用｜
-t	| 为容器重新分配一个伪输入终端，通常与 -i 同时使用｜
-d	| 后台运行容器，并返回容器ID｜


## Topic & Partition

### Topic
- 逻辑概念，同一个Topic的消息可分布在一个或多个节点（Broker）上
- 一个Topic包含一个或多个Partition
- 每条消息都属于且仅属于一个Topic
- Producer发布数据时，必须指定该消息发布到哪一个Topic
- Consumer订阅消息时，也必须指定订阅哪个Topic的消息

### Partition
- 物理概念，一个Partition只分布于一个Broker（不考虑备份）
- 一个Partition物理上对应一个文件夹
- 一个Partition包含多个Segment(Segment对用于透明)
- 一个Segment对应一个文件
- Segment由一个个不可变记录组成
- 记录只会被append到Segment中，不会被单独删除或者修改
- 清除过期日志时，直接删除一个或多个Segment


## kafka相关概念
1.AMQP协议

          push           pull
producer ——————  broker —————— consumer
- 消费者（consumer）：从消息队列中请求消息的客户端程序
- 生产者（producer）：向broker发布消息的客户端程序
- AMQP服务器端（broker）：用来接收生产者发生的消息并将这些消息路由给服务器中的队列


2.支持大部分主流语言，包含c,c++,Erlang,Java,.net,perl,PHP,Python,Go等

3.kafka架构

producer   producer  producer

       kafka cluster
consumer   consumer  consumer


- 主题（Topic）：一个主题类似新闻中的体育、娱乐、教育等分类概念，在实际工程中通常一个业务一个主题；
- 分区（Partition）：一个topic中的消息数据按照多个分区组织，分区是kafka消息队列组织的最小单位，一个分区可以看做是一个FIFO的队列;

![kafka架构](/Users/lynch/Documents/Idea/Kafka_study/images/kafka架构.png)

## zookeeper集群搭建
1.集群搭建
> docker上搭建3台zookeeper集群
docker run -itd --name zookeeper -h zookeeper -p 2181:2181 lynch/zookeeper:3.4.6 bash
docker run -itd --name zookeeper2 -h zookeeper2 -p 2182:2182 lynch/zookeeper:3.4.6 bash
docker run -itd --name zookeeper3 -h zookeeper3 -p 2183:2183 lynch/zookeeper:3.4.6 bash
zookeeper配置文件
```properties
 The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=/opt/zookeeper/zkdata
dataLogDir=/opt/zookeeper/zkdataLog
# the port at which the clients will connect
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
server.1=172.17.0.2:2888:3888
server.2=172.17.0.4:2888:3888
server.3=172.17.0.5:2888:3888
```

在/opt/zookeeper/zkdata的文件下设置myid
echo "1" > myid

server.1=172.17.0.2:2888:3888
server.2=172.17.0.4:2888:3888
server.3=172.17.0.5:2888:3888
- leader和slave通信的端口2888
- 集群出现问题，或者集群刚启动时，leader选取的端口3888，

2.参数介绍