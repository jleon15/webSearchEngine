package webSearchEngine.ws.rest;

import core.result.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@Scope("request")
@Path("/test")
public class TestResource {

    @Autowired
    ResultService service;

    @GET
    public Response testService(){
        this.service.sayHello();
        return Response.ok().build();
    }

}
