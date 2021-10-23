package edu.buffalo.ds.service;

import edu.buffalo.ds.database.daos.SubscriberDAO;
import edu.buffalo.ds.database.daos.TopicDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public class TopicService {
    private TopicDAO topicDAO;
    private SubscriberDAO subscriberDAO;

    public Boolean publishEvents(String topicName, String rate)
    {
        Boolean topicStatus =  topicDAO.refreshTopic(topicName, rate);
        log.info("Topic Refresh status : {}", topicStatus);
        if(Boolean.TRUE.equals(topicStatus)) {
            subscriberDAO.changeStatusTrue(topicName);
            subscriberDAO.changeRate(topicName, rate);
            return true;
        }
        else
        {
            return false;
        }
    }
}
