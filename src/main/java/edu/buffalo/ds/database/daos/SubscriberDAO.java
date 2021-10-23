package edu.buffalo.ds.database.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import edu.buffalo.ds.database.MongoDBFactoryConnection;
import edu.buffalo.ds.models.Subscriber;
import edu.buffalo.ds.models.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SubscriberDAO {
    private static final String TOPICS = "topics";
    private static final String IS_CHANGE = "isChange";
    private final MongoCollection<Document> subscriberCollection;
    public SubscriberDAO(MongoDBFactoryConnection mongoDBFactoryConnection) {
        this.subscriberCollection = mongoDBFactoryConnection.getClient()
                .getDatabase(mongoDBFactoryConnection.getMongoDBConnection().getDatabase())
                .getCollection("subscribers");

    }



    public List<Subscriber> getSubscriberWithId(String subscriberId)
    {
        final MongoCursor<Document> subscribers =
                subscriberCollection.find(Filters.eq("id", subscriberId)).iterator();

        List<Subscriber> result = new ArrayList<>();

        try
        {
            while(subscribers.hasNext())
            {
                Document doc = subscribers.next();
                result.add(mapSubscriberFrom(doc));
            }
        }catch (Exception e)
        {
            log.error("Error while fetching subscriber", e);

            //Do nothing
        }
        finally {
            subscribers.close();
        }
        return result;
    }

    public Optional<Subscriber> getSubscriberWithChanges(String subscriberId, String reload)
    {
        List<Subscriber> result = getSubscriberWithId(subscriberId);

        // Result cannot be more than one
        if(result.size() == 1)
        {
            Subscriber temp = result.get(0);
            log.info("Found 1 subscriber : {}", temp);
            //For reload send all details
            if(StringUtils.compareIgnoreCase(reload, "true") == 0)
            {
                log.info("Reload is true");
                temp.setIsChange(Boolean.TRUE);
            }

            if(temp.getIsChange().equals(Boolean.FALSE)) {
                log.info("Returning no topics as No change detected");
                return Optional.of(new Subscriber(temp.getIsChange(),temp.getSubscriberId(),null));
            }
            else
            {
                makeChangeFalse(temp.getSubscriberId());
                return Optional.of(temp);
            }

        }
        else
        {
            return Optional.empty();
        }
    }

    public void makeChangeFalse(String subscriberId) {
        log.info("Changing status to false for subscriber : {}", subscriberId);
        subscriberCollection
                .findOneAndUpdate(Filters.eq("id", subscriberId), Updates.set(IS_CHANGE, Boolean.FALSE));
    }
    public void makeAllChangeTrue() {
        log.info("Changing status to true for all subscriber");
        subscriberCollection
                .updateMany(Filters.eq(IS_CHANGE, Boolean.FALSE), Updates.set(IS_CHANGE, Boolean.TRUE));
    }


    private Subscriber mapSubscriberFrom(Document doc) {
        List<Document> documents = (List<Document>) doc.get(TOPICS);
        List<Topic> topics = new ArrayList<>();
        for (Document d : documents)
        {
            topics.add(new Topic(d.getString("id"), d.getString("name"), d.getString("subscribed")
                    , d.getString("message")));
        }
        return new Subscriber(doc.getBoolean(IS_CHANGE), doc.getString("id")
                , topics);
    }


    public boolean saveSubscriber(Subscriber subscriber) {
        boolean isSaved = true;
        Document document =  new Document("id", subscriber.getSubscriberId())
                .append(IS_CHANGE, Boolean.FALSE)
                .append(TOPICS, subscriber.getTopics());

        try {
            log.info("Saving document : {}", document.toString());
            subscriberCollection.insertOne(document);
        }
        catch (Exception e)
        {
            log.error("Error in saving user", e);
            isSaved = false;
        }

        return  isSaved;
    }

    public void subscribeToTopic(String subscriberId, String topicName) {

        Bson update = Updates.set("topics.$[j].subscribed", "true");
        Bson filter2 = Filters.eq("j.name", topicName);
        UpdateOptions updateOptions = new UpdateOptions().arrayFilters(Arrays.asList( filter2));
        UpdateResult res = subscriberCollection.updateOne(Filters.eq("id", subscriberId ), update, updateOptions);
        log.info("Update Result : {} ", res);


    }

    public void changeStatus(String topicName) {
        Bson update = Updates.set("topics.$[i].isChange", "true");
        Bson filter1 = Filters.eq("i.name", topicName );
        UpdateOptions updateOptions = new UpdateOptions().arrayFilters(List.of(filter1));
        UpdateResult res = subscriberCollection.updateOne(new BsonDocument(new ArrayList<>()), update, updateOptions);
        log.info("Update Result : {} ", res);
    }

    public void appendTopic(Topic topic) {
        subscriberCollection.updateMany(new Document(), Updates.push(TOPICS, topic) );
    }
}
