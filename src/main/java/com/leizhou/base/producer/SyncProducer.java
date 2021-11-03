package com.leizhou.base.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

public class SyncProducer {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("group1_1234");

        producer.setNamesrvAddr("localhost:9876;localhost:9877");
//        producer.setSendMsgTimeout(3000);

//          producer.setVipChannelEnabled(false);
        producer.start();

//        String createTopicKey = producer.getCreateTopicKey();
//        producer.createTopic(createTopicKey, "base1", 3);
        for (int i = 0; i < 1000; i++) {
            Message message = new Message("base2", "tag1", ("Hellofff " + i).getBytes());

            SendResult result = producer.send(message, 100000);

            SendStatus sendStatus = result.getSendStatus();
            int queueId = result.getMessageQueue().getQueueId();
            String msgId = result.getMsgId();

            System.out.println("Broker :" + result.getMessageQueue().getBrokerName() +
                    " send status " + sendStatus + " msgId: " + msgId + " queue id: " + queueId);
//            TimeUnit.SECONDS.sleep(1);
        }
        producer.shutdown();
    }
}
