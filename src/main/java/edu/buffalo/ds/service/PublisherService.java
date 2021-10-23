package edu.buffalo.ds.service;

import edu.buffalo.ds.database.daos.PublisherDAO;
import edu.buffalo.ds.database.daos.TopicDAO;
import edu.buffalo.ds.models.Publisher;
import edu.buffalo.ds.models.Topic;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class PublisherService {
    private PublisherDAO publisherDAO;
    private TopicDAO topicDAO;
    public Optional<Publisher> getPublisher(String publisherId)
    {
        List<Publisher> publisherList = publisherDAO.getPublishers(publisherId);

        if(publisherList.size() != 1)
        {
            log.info("Publisher List size : {} for ID : {}",publisherList.size(), publisherId);
            return Optional.empty();
        }
        else return Optional.of(publisherList.get(0));
    }
    public Boolean saveTopicToPublisher(String publisherId, String topicName)
    {
        Optional<Publisher> optionalPublisher = getPublisher(publisherId);
        Optional<Topic> topic  = topicDAO.getTopicUsingName(topicName);

        if(topic.isPresent())
        {
            return Boolean.FALSE;
        }
        else
        {
            topicDAO.saveTopic(topicName);
        }

        if(optionalPublisher.isEmpty())
        {
            Publisher publisher = new Publisher(publisherId, List.of(topicName));
            publisherDAO.save(publisher);
        }
        else
        {
            publisherDAO.appendTopics(publisherId, topicName);
        }
        return Boolean.TRUE;
    }
}
