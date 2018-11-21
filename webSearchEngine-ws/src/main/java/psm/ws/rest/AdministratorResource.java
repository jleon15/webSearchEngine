package psm.ws.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.security.administrator.service.AdministratorService;
import programathon.psm.model.Administrator;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Scope("request")
@Path("/administrator")
public class AdministratorResource {

    @Autowired
    private AdministratorService administratorService;

    /**
     * Corresponds to the web service in charge of creating a new account for the user when signing up.
     * @param username corresponds to the user's complete name.
     * @param password corresponds to the user's email, must be unique.
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createNewAccount(@FormParam("username") String username,
                                     @FormParam("password") String password) {


        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Administrator administrator = new Administrator();
        administrator.setName("Josue");
        administrator.setUsername(username);
        administrator.setPassword(password);
        this.administratorService.create(administrator);

        return Response.ok().build();
    }
}
