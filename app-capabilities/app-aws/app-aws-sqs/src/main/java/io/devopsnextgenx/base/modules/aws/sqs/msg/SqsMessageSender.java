package io.devopsnextgenx.base.modules.aws.sqs.msg;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

@Slf4j
@AllArgsConstructor
public class SqsMessageSender {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final QueueNameValidator queueNameValidator;

    /**
     *
     * @param queueName queueName to be pushed, basic name to be validated
     * @param msgObject
     */
    public boolean pushMsgToQueue(String queueName, Object msgObject) {
        String queueFullName = queueNameValidator.validateAndAddPrefixToQueueName(queueName);
        if (queueFullName != null) {
            queueMessagingTemplate.convertAndSend(queueFullName, msgObject);
            log.info("QueueClient Sent msg to queue :{} ,Msg:{}", queueFullName, msgObject);
            return true;
        } else {
            log.warn("QueueName null, can't send message!!!");
            return false;
        }
    }
}
