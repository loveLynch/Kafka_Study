package com.lynch.producer;

import com.lynch.domain.Company;
import com.lynch.producer.utils.CompanySerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Created by lynch on 2020-04-16.
 **/
public class KafkaProducerCompany {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-test";


    public static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CompanySerializer.class.getName());
        properties.put("bootstrap.servers", brokerList);
        return properties;
    }

    public static void main(String[] args) {

        Properties properties = initConfig();

        KafkaProducer<String, Company> producer = new KafkaProducer<>(properties);

        Company company = Company.builder().name("hiddenkafka")
                .address("China").build();

        //构建需要发送的消息
        ProducerRecord<String, Company> record = new ProducerRecord<>(topic, company);
        //发送消息
        try {
            producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //关闭生产者客户端示例
        producer.close();
    }
}
