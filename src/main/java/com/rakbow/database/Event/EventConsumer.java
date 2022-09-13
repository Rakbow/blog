package com.rakbow.database.Event;

import com.rakbow.database.service.util.common.CommonConstant;
import org.springframework.stereotype.Component;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-09-13 19:53
 * @Description:
 */
@Component
public class EventConsumer implements CommonConstant {

    // private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //
    // @Autowired
    // private MessageService messageService;
    //
    // @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    // public void handleCommentMessage(ConsumerRecord record) {
    //     if (record == null || record.value() == null) {
    //         logger.error("消息的内容为空!");
    //         return;
    //     }
    //
    //     Event event = JSONObject.parseObject(record.value().toString(), Event.class);
    //     if (event == null) {
    //         logger.error("消息格式错误!");
    //         return;
    //     }
    //
    //     // 发送站内通知
    //     Message message = new Message();
    //     message.setFromId(SYSTEM_USER_ID);
    //     message.setToId(event.getEntityUserId());
    //     message.setConversationId(event.getTopic());
    //     message.setCreateTime(new Date());
    //
    //     Map<String, Object> content = new HashMap<>();
    //     content.put("userId", event.getUserId());
    //     content.put("entityType", event.getEntityType());
    //     content.put("entityId", event.getEntityId());
    //
    //     if (!event.getData().isEmpty()) {
    //         for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
    //             content.put(entry.getKey(), entry.getValue());
    //         }
    //     }
    //
    //     message.setContent(JSONObject.toJSONString(content));
    //     messageService.addMessage(message);
    // }
}
