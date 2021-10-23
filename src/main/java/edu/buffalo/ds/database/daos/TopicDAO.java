package edu.buffalo.ds.database.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.buffalo.ds.database.MongoDBFactoryConnection;

import edu.buffalo.ds.models.Topic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TopicDAO {
    private static final String MSG = "message";
    private final MongoCollection<Document> topicCollection;
    public TopicDAO(MongoDBFactoryConnection mongoDBFactoryConnection) {
        this.topicCollection = mongoDBFactoryConnection.getClient()
                .getDatabase(mongoDBFactoryConnection.getMongoDBConnection().getDatabase())
                .getCollection("topics");
    }

    public List<Topic> getAllTopics()
    {
        final MongoCursor<Document> topics = topicCollection.find().iterator();
        List<Topic> result = new ArrayList<>();
        try
        {
            while(topics.hasNext())
            {
                Document doc = topics.next();
                result.add(mapTopicFrom(doc));
            }
        }catch (Exception e)
        {
            log.error("Error while fetching topics");

            //Do nothing
        }
        finally {
            topics.close();
        }
        return result;
    }


    public Optional<Topic> getTopicUsingName(String topicName)
    {
        final MongoCursor<Document> topics =
                topicCollection.find(Filters.eq("name", topicName)).iterator();
        List<Topic> result = new ArrayList<>();
        try
        {
            while(topics.hasNext())
            {
                Document doc = topics.next();
                result.add(mapTopicFrom(doc));
            }
        }catch (Exception e)
        {
            log.error("Error while fetching users");

            //Do nothing
        }
        finally {
            topics.close();
        }
        if(!result.isEmpty()) {
            return Optional.of(result.get(0));
        }
        else
        {
            return Optional.empty();
        }
    }

    public static Topic mapTopicFrom(Document doc) {
        return new Topic(doc.getObjectId("_id").toString()
                , doc.getString("name"), "", doc.getString(MSG));
    }

    public void saveTopic(String topicName)
    {
        Document document = new Document("name", topicName)
                .append(MSG, "");
        log.info("Saving topic document : {}", document.toString());
        topicCollection.insertOne(document);
    }


    public Boolean refreshTopic(String topicName, String rate) {
        Optional<Topic> optionalTopic = getTopicUsingName(topicName);
        if(optionalTopic.isEmpty())
        {
            log.error("No topic found in DB for name : {}", topicName);
            return Boolean.FALSE;
        }
        Topic topic = optionalTopic.get();
        if(StringUtils.compareIgnoreCase(topic.getMessage(), rate) == 0)
        {
            log.error("Topic has same rate as last one, ignoring new event with rate : {}", rate);
            return  Boolean.FALSE;
        }
        topicCollection.findOneAndUpdate(Filters.eq("name", topicName), Updates.set(MSG, rate));
        return Boolean.TRUE;
    }
}
