package com.leizhou.filter.bysql;

import com.leizhou.order.OrderStep;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Producer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("group_order");

        producer.setNamesrvAddr("localhost:9876;localhost:9877");
//        producer.setSendMsgTimeout(3000);

//          producer.setVipChannelEnabled(false);
        producer.start();
        int count = 0;
        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0 ; i < 10 ; i++) {
            Message msg = new Message("FilterBySqlTopic", "tag2", String.valueOf(count++), String.valueOf(i).getBytes());
            msg.putUserProperty("a", String.valueOf(i));
            messages.add(msg);
        }

        SendResult sendResult = producer.send(messages);

        System.out.println("Send result : " +sendResult);
        producer.shutdown();
        System.out.println("Producer send done");
    }
}
