package edu.buffalo.ds.resources;

import edu.buffalo.ds.models.User;
import edu.buffalo.ds.service.UserInfoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public class ForexResource {

    private final UserInfoService userInfoService;
    public ForexResource(UserInfoService userInfoService)
    {

        this.userInfoService = userInfoService;
    }

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


}
