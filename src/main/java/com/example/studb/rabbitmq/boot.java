package com.example.studb.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * boot
 *
 * @author zhangchao01
 * @date 2021/6/17
 */
@Component
public class boot implements CommandLineRunner {

    @Autowired
    private ProducerDelay producer;

    @Override
    public void run(String... args) throws Exception {
        producer.produce2();
        producer.produceD();
        Thread.sleep(10000);
    }


}
