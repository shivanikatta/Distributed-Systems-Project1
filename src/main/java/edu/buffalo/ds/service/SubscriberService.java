package edu.buffalo.ds.service;

import edu.buffalo.ds.database.daos.SubscriberDAO;
import edu.buffalo.ds.database.daos.TopicDAO;
import edu.buffalo.ds.models.Subscriber;
import edu.buffalo.ds.models.Topic;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class SubscriberService {

    private  TopicDAO topicDAO;
    private  SubscriberDAO subscriberDAO;

    public Subscriber pollOrAddSubscriber(String subscriberId, String reload) {
        Optional<Subscriber> optionalSubscriber = subscriberDAO.getSubscriberWithChanges(subscriberId, reload);

        if(optionalSubscriber.isPresent())
        {
            log.info("Found one Subs");
            return  optionalSubscriber.get();

        }
        log.info("Didn't find any subs");
        List<Topic> allTopics = topicDAO.getAllTopics();
        List<Topic> topicList = new ArrayList<>();

        for (Topic real : allTopics)
        {
            Topic temp = new Topic(real.getTopicId(), real.getTopicName()
                    , Boolean.FALSE.toString(), "");
            topicList.add(temp);
        }

        Subscriber subscriber = new Subscriber(Boolean.TRUE, subscriberId, topicList);
        subscriberDAO.saveSubscriber(subscriber);
        return subscriber;
    }

    public Boolean subscribeToTopic(String subscriberId, String topicName) {
        List<Subscriber> subscribers = subscriberDAO.getSubscriberWithId(subscriberId);
        if(subscribers.size() != 1)
        {
            log.error("Subscriber not found with Id : {}", subscriberId);
            return Boolean.FALSE;
        }
        Optional<Topic> optionalTopic = topicDAO.getTopicUsingName(topicName);
        if(optionalTopic.isEmpty())
        {
            log.error("Topic not found with Name : {}", topicName);
            return Boolean.FALSE;
        }
        subscriberDAO.subscribeToTopic(subscriberId, topicName);
        return Boolean.TRUE;
    }


    public Boolean appendTopicToExistingSubscribers(String topicName) {
        Optional<Topic> topicOptional = topicDAO.getTopicUsingName(topicName);
        if(topicOptional.isEmpty())
        {
            return Boolean.FALSE;
        }
        Topic newTopic = topicOptional.get();
        newTopic.setSubscribed(Boolean.FALSE.toString());
        subscriberDAO.appendTopic(newTopic);
        subscriberDAO.makeAllChangeTrue();
        return Boolean.TRUE;
    }

    public Boolean unsubscribeToTopic(String subscriberId, String topicName) {
        List<Subscriber> subscribers = subscriberDAO.getSubscriberWithId(subscriberId);
        if(subscribers.size() != 1)
        {
            log.error("Subscriber not found with Id : {}", subscriberId);
            return Boolean.FALSE;
        }
        Optional<Topic> optionalTopic = topicDAO.getTopicUsingName(topicName);
        if(optionalTopic.isEmpty())
        {
            log.error("Topic not found with Name : {}", topicName);
            return Boolean.FALSE;
        }
        subscriberDAO.unsubscribeToTopic(subscriberId, topicName);
        return Boolean.TRUE;
    }
}
