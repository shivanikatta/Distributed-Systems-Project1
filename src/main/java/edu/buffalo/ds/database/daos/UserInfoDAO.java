package edu.buffalo.ds.database.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.buffalo.ds.database.MongoDBFactoryConnection;
import edu.buffalo.ds.models.User;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class UserInfoDAO {
    private final MongoCollection<Document> userCollection;

    public UserInfoDAO(MongoDBFactoryConnection mongoDBFactoryConnection) {
        this.userCollection = mongoDBFactoryConnection.getClient()
                .getDatabase(mongoDBFactoryConnection.getMongoDBConnection().getDatabase())
                .getCollection("users");
    }


    public List<User>  getAllUsers()
    {
        final MongoCursor<Document> users = userCollection.find().iterator();
        List<User> result = new ArrayList<>();
        try
        {
            while(users.hasNext())
            {
                Document doc = users.next();
                result.add(mapUserFromDocument(doc));
            }
        }catch (Exception e)
        {
           log.error("Error while fetching users");

            //Do nothing
        }
        finally {
            users.close();
        }
        return result;
    }

    public boolean save(final User user)
    {
        boolean isSaved = true;
        Document document = new Document("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("country", user.getCountry());

        try {
            log.info("Saving document : {}", document.toString());
            userCollection.insertOne(document);
        }
        catch (Exception e)
        {
            log.error("Error in saving user");
            isSaved = false;
        }

        return  isSaved;

    }

    private User mapUserFromDocument(final Document doc)
    {
        return new User(doc.getObjectId("_id")
                , doc.getString("firstName")
                , doc.getString("lastName")
                , doc.getString("country"));
    }

}
