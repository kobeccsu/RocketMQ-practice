package com.leizhou.filter.bysql;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_order");

        consumer.setNamesrvAddr("localhost:9876;localhost:9877");
        consumer.subscribe("FilterBySqlTopic", MessageSelector.bySql("a>5"));

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt msg : list) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " ,got message " + new String(msg.getBody())
                            + " queue id is :" + msg.getQueueId() + " delayed " + (System.currentTimeMillis() - msg.getStoreTimestamp()));
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer starting...");
    }
}
