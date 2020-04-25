package com.lynch.consumer;

import com.lynch.consumer.utils.CompanyDeserializer;
import com.lynch.domain.Company;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by lynch on 2020-04-20.
 **/
@Slf4j
public class KafkaConsumerCompany {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-test";
    public static final String groupId = "group.test";

    public static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CompanyDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "client.id.test");
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        KafkaConsumer<String, Company> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));

        //循环消费消息
        while (true) {

            try {
                ConsumerRecords<String, Company> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, Company> record : records) {
                    System.out.println("topic  = " + record.topic() + ", partition = " + record.partition() + ", offset = " + record.offset());
                    System.out.println("key = " + record.key() + ", value = " + record.value());
                }
            } catch (Exception e) {
                log.error("occur exception ", e);
            }

        }
    }
}
