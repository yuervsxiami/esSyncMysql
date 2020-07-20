package com.yzf.esdemo.controller;

import com.yzf.esdemo.dto.VoucherDTO;
import com.yzf.esdemo.kafka.KafkaSender;
import com.yzf.esdemo.listener.JsonConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2020/07/16
 * Time: 4:01 下午
 */

@RestController
public class TestController {

    // kafka话题
    @Value("${kafka.topic}")
    private String topic;

    // kafka分区
    @Value("${kafka.partNum}")
    private int partNum;

    // Kafka备份数
    @Value("${kafka.repeatNum}")
    private short repeatNum;

    // kafka地址
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaHost;

    @Autowired
    KafkaSender kafkaSender;

    @Autowired
    private JsonConsumer jsonConsumer;

    @GetMapping("/test")
    public void test(){
        kafkaSender.createTopic(kafkaHost, topic, partNum, repeatNum);
        kafkaSender.send(topic,"testhhahaa");
    }

    @GetMapping("/save")
    public void saveEl(){
        VoucherDTO build = VoucherDTO.builder().id(1111l)
                .bbh(111l)
                .fhr("1")
                .fhrq(new Date())
                .fjs(1)
                .jlrq(new Date())
                .kjnd(1)
                .kjqj(1)
                .kprq(new Date())
                .lsh(1)
                .ly("1")
                .pzz(1)
                .xgr("1")
                .ztdm(111111l).build();

        jsonConsumer.doAddList(Collections.singletonList(build));
    }
}
