package com.leizhou.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

public class OrderProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("group_order");

        producer.setNamesrvAddr("localhost:9876;localhost:9877");
//        producer.setSendMsgTimeout(3000);

//          producer.setVipChannelEnabled(false);
        producer.start();
        List<OrderStep> orders = OrderStep.buildOrders();
        int count = 0;
        for (OrderStep order : orders) {
            Message msg = new Message("OrderTopic", "order", String.valueOf(count++), order.toString().getBytes());
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    long orderId = (long) o;
                    long queueIndex = orderId % list.size();
                    return list.get((int) queueIndex);
                }
            }, order.getOrderId());

            System.out.println("Send result : " +sendResult);

        }
        producer.shutdown();
        System.out.println("Producer send done");

    }
}
