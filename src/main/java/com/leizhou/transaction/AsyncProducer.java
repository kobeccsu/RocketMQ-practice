package com.leizhou.transaction;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;


public class AsyncProducer {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("group_trans");

        producer.setNamesrvAddr("localhost:9877;localhost:9876");
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                if ("TagA".equals(message.getTags())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if ("TagB".equals(message.getTags())) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    return LocalTransactionState.UNKNOW;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("check local transaction : " + messageExt.getTags());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        producer.start();

        String[] tags = {"TagA", "TagB", "TagC"};
        for (int i = 0; i < tags.length; i++) {
            Message message = new Message("TransTopic", tags[i], ("Hello fff " + i).getBytes());

            SendResult result = producer.sendMessageInTransaction(message, null);

            SendStatus sendStatus = result.getSendStatus();
            int queueId = result.getMessageQueue().getQueueId();
            String msgId = result.getMsgId();

            System.out.println("Broker :" + result.getMessageQueue().getBrokerName() +
                    " send status " + sendStatus + " msgId: " + msgId + " queue id: " + queueId);
        }

        //producer.shutdown();
    }
}
