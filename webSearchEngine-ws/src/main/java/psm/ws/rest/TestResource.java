package psm.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.naturalLanguage.service.impl.IbmNaturalLanguageService;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Scope("request")
@Path("/test")
public class TestResource {

    @Autowired
    IbmNaturalLanguageService service;

    @GET
    public Response testNLU(){
        this.service.analyzeText("La mandarina es roja");
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/post")
    public Response postTest(@FormParam("post") String test) {
        return Response.ok().entity(test).build();
    }

}
