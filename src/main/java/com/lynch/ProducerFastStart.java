package com.lynch;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by lynch on 2020-04-14.
 **/
public class ProducerFastStart {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-test";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);
        //配置生产者客户端参数并创建kafkaProducer实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //构建需要发送的消息
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello,kafka!  ");
        //发送消息
        try {
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //关闭生产者客户端示例
        producer.close();
    }
}
