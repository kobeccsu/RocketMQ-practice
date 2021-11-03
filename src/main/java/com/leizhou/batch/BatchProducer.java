package com.leizhou.batch;

import com.leizhou.order.OrderStep;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.List;

public class BatchProducer {
    public static void main(String[] args) throws  Exception{
        DefaultMQProducer producer = new DefaultMQProducer("group_order");

        producer.setNamesrvAddr("localhost:9876;localhost:9877");
//        producer.setSendMsgTimeout(3000);

//          producer.setVipChannelEnabled(false);
        producer.start();
        List<OrderStep> orders = OrderStep.buildOrders();
        int count = 0;
        ArrayList<Message> messages = new ArrayList<>();
        for (OrderStep order : orders) {
            Message msg = new Message("BatchTopic", "tag", String.valueOf(count++), order.toString().getBytes());
//            msg.setDelayTimeLevel(2);
            messages.add(msg);




        }
        SendResult sendResult = producer.send(messages);

        System.out.println("Send result : " +sendResult);
        producer.shutdown();
        System.out.println("Producer send done");

    }
}
