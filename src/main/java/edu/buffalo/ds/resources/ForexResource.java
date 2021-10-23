package edu.buffalo.ds.resources;

import edu.buffalo.ds.api.AdvertiseRequest;
import edu.buffalo.ds.api.PublishRequest;
import edu.buffalo.ds.api.SubscribeRequest;
import edu.buffalo.ds.models.Subscriber;
import edu.buffalo.ds.models.User;
import edu.buffalo.ds.service.PublisherService;
import edu.buffalo.ds.service.SubscriberService;
import edu.buffalo.ds.service.TopicService;
import edu.buffalo.ds.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Slf4j
@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class ForexResource {

    private final UserInfoService userInfoService;
    private final PublisherService publisherService;
    private final SubscriberService subscriberService;
    private final TopicService topicService;

    @GET
    @Path("/hello")
    public String sayHello(@QueryParam("name") Optional<String> name)

    {
        return "Hello World";
    }

    @GET
    @Path("/get-all")
    public Response getAllUsers()
    {
        return Response.ok().entity(userInfoService.getAllUsers()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save-user")
    public Response saveUser(User user)
    {
        return Response.ok().entity(userInfoService.saveUser(user)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/publish")
    public Response publish(PublishRequest publishRequest)
    {
        log.info(publishRequest.toString());
        Boolean res = topicService.publishEvents(publishRequest.getTopicName(), publishRequest.getRate());
        if(res.equals(Boolean.FALSE))
        {
            return Response.status(400,"Bad Topic Name or Subscriber Id or Redundant Rates").build();
        }
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/advertise")
    public Response advertise(AdvertiseRequest advertiseRequest)
    {
        log.info(advertiseRequest.toString());
        Boolean state = publisherService.saveTopicToPublisher(advertiseRequest.getPublisherId(), advertiseRequest.getTopicName());
        if(state.equals(Boolean.FALSE))
        {
            return Response.status(400,"Bad Topic Name").build();
        }
        state = subscriberService.appendTopicToExistingSubscribers(advertiseRequest.getTopicName());
        if(state.equals(Boolean.FALSE))
        {
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    //Get all topics for subscriber

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/poll/{subscriberId}")
    public Response pollEvents(@PathParam("subscriberId") String subscriberId
            , @QueryParam("reload") String reload)
    {
        Subscriber subscriber = subscriberService.pollOrAddSubscriber(subscriberId, reload);
        return  Response.ok().entity(subscriber).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/subscribe")
    public Response subscribeTopic(SubscribeRequest subscribeRequest)
    {
        Boolean result = subscriberService.subscribeToTopic(subscribeRequest.getSubscriberId()
                , subscribeRequest.getTopicName());
        if(result.equals(Boolean.FALSE))
        {
            return Response.status(400,"Bad Subscriber Id or Topic Id").build();
        }
        return Response.ok().entity("SUCCESS").build();

    }

}
