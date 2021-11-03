package com.leizhou.batch;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BatchConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_order");

        consumer.setNamesrvAddr("localhost:9876;localhost:9877");
        consumer.subscribe("BatchTopic", "tag");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeOrderlyContext) {
                for (MessageExt msg : list) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " ,got message " + new String(msg.getBody())
                            + " queue id is :" + msg.getQueueId() + " delayed " + (System.currentTimeMillis() - msg.getStoreTimestamp()));
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer starting...");
//        TimeUnit.SECONDS.sleep(15);
//        consumer.shutdown();
    }
}
