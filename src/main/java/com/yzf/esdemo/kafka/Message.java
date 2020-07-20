package com.yzf.esdemo.kafka;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2020/07/15
 * Time: 2:11 下午
 */

@Data
public class Message {

    private Long id;

    private String msg;

    private Date sendTime;
}
