package edu.buffalo.ds.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class ForexResource {

    private String defaultName;
    private final AtomicLong counter;
    public ForexResource(String defaultName)
    {
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    public String sayHello(@QueryParam("name") Optional<String> name)

    {
        final String value = name.orElse(defaultName);
        return value;
    }

}
