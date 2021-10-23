package edu.buffalo.ds.database.daos;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.buffalo.ds.database.MongoDBFactoryConnection;
import edu.buffalo.ds.models.Publisher;
import edu.buffalo.ds.models.User;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PublisherDAO {
    private final MongoCollection<Document> publisherCollection;
    private static final String TOPICS = "topics";
    public PublisherDAO(MongoDBFactoryConnection mongoDBFactoryConnection) {
        this.publisherCollection = mongoDBFactoryConnection.getClient()
                .getDatabase(mongoDBFactoryConnection.getMongoDBConnection().getDatabase())
                .getCollection("publishers");
    }

    public boolean save(final Publisher publisher)
    {
        boolean isSaved = true;
        Document document = new Document("id", publisher.getPublisherId())
                .append(TOPICS, publisher.getTopics());

        try {
            log.info("Saving document : {}", document.toString());
            publisherCollection.insertOne(document);
        }
        catch (Exception e)
        {
            log.error("Error in saving user");
            isSaved = false;
        }

        return  isSaved;
    }
    public void appendTopics(String publisherId, String topic)
    {
        publisherCollection.findOneAndUpdate(Filters.eq("id", publisherId), Updates.push(TOPICS, topic));
    }

    public List<Publisher> getPublishers(String publisherId)
    {
        final MongoCursor<Document> publishers =
                publisherCollection.find(Filters.eq("id",publisherId)).iterator();
        List<Publisher> result = new ArrayList<>();
        try
        {
            while(publishers.hasNext())
            {
                Document doc = publishers.next();
                result.add(mapPublisherFrom(doc));
            }
        }catch (Exception e)
        {
            log.error("Error while fetching users");

            //Do nothing
        }
        finally {
            publishers.close();
        }

        return result;

    }

    private Publisher mapPublisherFrom(Document doc) {

        return new Publisher(doc.getString("id")
                , doc.getList(TOPICS, String.class));
    }
}
