package com.yzf.esdemo.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yzf.esdemo.dto.VoucherDTO;
import com.yzf.esdemo.repository.ElVoucherRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2020/07/16
 * Time: 5:09 下午
 */

@Component
public class JsonConsumer {

    @Value("${es.format}")
    private String voucherFormat;

    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    ElVoucherRepository elVoucherRepository;

    @KafkaListener(topics = "binlog", id = "consumer1", containerFactory = "batchFactory")
    public void listen(List<ConsumerRecord<?, ?>> list) {
        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<?, ?> record : list) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            // 获取消息
            kafkaMessage.ifPresent(o -> messages.add(o.toString()));
        }
        if (messages.size() > 0) {
            // 更新索引
            updateES(messages);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateES(List<String> messages) {
        List<VoucherDTO> updateList = new ArrayList<>();
        List<VoucherDTO> addList = new ArrayList<>();
        List<VoucherDTO> deleteList = new ArrayList<>();
        for (String message : messages) {
            JSONObject result = null;
            try {
                result = JSON.parseObject(message);
            } catch (Exception e) {
                continue;
            }
            // 获取事件类型 event:"wtv3.videos.insert"
            String event = (String) result.get("event");
            String[] eventArray = event.split("\\.");
            String tableName = eventArray[1];
            String eventType = eventArray[2];
            // 获取具体数据
            JSONArray valueStr = (JSONArray) result.get("value");
            // 转化为对应格式的json字符串
            JSONObject object = getESObject(valueStr, tableName);
            VoucherDTO voucherDTO = object.toJavaObject(VoucherDTO.class);
            // 获取ES的type
            switch (eventType) {
                case "insert": {
                    addList.add(voucherDTO);
                    break;
                }
                case "update": {
                    // 更新videos
                    updateList.add(voucherDTO);
                    break;
                }
                case "delete": {
                    // 删除videos
                    deleteList.add(voucherDTO);
                    break;
                }
            }
            doAddList(addList);
            doUpdateList(updateList);
            doDeleteList(deleteList);
        }


    }

    private void doDeleteList(List<VoucherDTO> deleteList) {
        if(CollectionUtils.isEmpty(deleteList)){
            return;
        }
        elVoucherRepository.deleteAll(deleteList);
    }

    private void doUpdateList(List<VoucherDTO> updateList) {
        if(CollectionUtils.isEmpty(updateList)){
            return;
        }
        for(VoucherDTO voucherDTO : updateList){
            elVoucherRepository.deleteById(voucherDTO.getId());
            elVoucherRepository.save(voucherDTO);
        }
    }

    public void doAddList(List<VoucherDTO> addList) {
        if(CollectionUtils.isEmpty(addList)){
            return;
        }
        elVoucherRepository.saveAll(addList);
    }


    private JSONObject getESObject(JSONArray message, String tableName) {
        JSONObject resultObject = new JSONObject();
        String format = voucherFormat;
        if (!format.isEmpty()) {
            JSONObject jsonFormatObject = JSON.parseObject(format);
            for (String key : jsonFormatObject.keySet()) {
                String[] formatValues = jsonFormatObject.getString(key).split(",");
                if (formatValues.length < 2) {
                    resultObject.put(key, message.get(jsonFormatObject.getInteger(key)));
                } else {
                    Object object = message.get(Integer.parseInt(formatValues[0]));
                    if (object == null) {
                        String[] array = {};
                        resultObject.put(key, array);
                    } else {
                        String objectStr = message.get(Integer.parseInt(formatValues[0])).toString();
                        String[] result = objectStr.split(formatValues[1]);
                        resultObject.put(key, result);
                    }
                }
            }
        }
        return resultObject;
    }
}
