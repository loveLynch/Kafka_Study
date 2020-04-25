package com.lynch.producer;

import com.lynch.producer.utils.DemoPartitioner;
import com.lynch.producer.utils.ProducerInterceptorPrefix;
import com.lynch.producer.utils.ProducerInterceptorPrefixPlus;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by lynch on 2020-04-16.
 **/
public class KafkaProducerAnalysis {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-test";


    public static Properties initConfig() {
        Properties properties = new Properties();
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //显示指定自定义分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DemoPartitioner.class.getName());
        //拦截器
//        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName());
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName() + "," + ProducerInterceptorPrefixPlus.class.getName());
        properties.put("bootstrap.servers", brokerList);
        properties.put("client.id", "producer.client.id.test");
        return properties;
    }

    public static void main(String[] args) {

        Properties properties = initConfig();

        //配置生产者客户端参数并创建kafkaProducer实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //构建需要发送的消息
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "kafka " + i);
            //发送消息
            try {
                producer.send(record);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //关闭生产者客户端示例
        producer.close();
    }
}
