package io.devopsnextgenx.base.modules.aws.sns.notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;

@Slf4j
@AllArgsConstructor
public class SnsNotifictionSender {
    private final NotificationMessagingTemplate notificationMessagingTemplate;
    private final TopicNameValidator topicNameValidator;

    /**
     *
     * @param topicName topicName to be pushed, basic name to be validated
     * @param msgObject
     */
    public boolean pushNotificationTotopic(String topicName, Object msgObject, String subject) {
        String topicFullName = topicNameValidator.validateAndAddPrefixToTopicName(topicName);
        if (topicFullName != null) {
            notificationMessagingTemplate.sendNotification(topicFullName, msgObject, subject);
            log.info("TopicClient Sent msg to topic :{} ,Msg:{}", topicFullName, msgObject);
            return true;
        } else {
            log.warn("topicName null, can't send message!!!");
            return false;
        }
    }
}
