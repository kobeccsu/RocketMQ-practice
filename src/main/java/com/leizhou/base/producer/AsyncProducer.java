package com.leizhou.base.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncProducer {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("group1_1234");

        producer.setNamesrvAddr("localhost:9877;localhost:9876");
//        producer.setSendMsgTimeout(3000);

//        producer.setVipChannelEnabled(false);
        producer.start();

//        String createTopicKey = producer.getCreateTopicKey();
//        producer.createTopic(createTopicKey, "base1", 3);
        int messageCount = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            Message message = new Message("base7", "tag7", ("Hello1 " + i).getBytes());

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();

                    SendStatus sendStatus = sendResult.getSendStatus();
                    int queueId = sendResult.getMessageQueue().getQueueId();
                    String msgId = sendResult.getMsgId();

                    System.out.println("Broker :" + sendResult.getMessageQueue().getBrokerName() +
                            " send status " + sendStatus + " msgId: " + msgId + " queue id: " + queueId);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onException(Throwable throwable) {
//                    throwable.printStackTrace();
                    countDownLatch.countDown();
                    System.out.println("Error : " + throwable);
                }
            });
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

        countDownLatch.await();
        producer.shutdown();
//        producer.shutdown();
    }
}
