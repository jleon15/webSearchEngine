package psm.ws.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.security.manager.service.ManagerService;
import programathon.psm.model.Manager;
import programathon.psm.support.SecurityUtils;
import programathon.psm.support.validation.ValidationUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Scope("request")
@Path("/manager-user")
public class ManagerResource {

    @Autowired
    private ManagerService managerService;

    /**
     * Corresponds to the web service in charge of creating a new account for the user when signing up.
     * @param fullName corresponds to the user's complete name.
     * @param email corresponds to the user's email, must be unique.
     * @param password corresponds to the user's password.
     * @return not found if any of the parameters is missing, bad request if the email is already taken, ok if everything went well.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createNewAccount(@FormParam("id") String id,
                                     @FormParam("fullName") String fullName,
                                     @FormParam("email") String email,
                                     @FormParam("password") String password,
                                     @FormParam("phoneNumber") String phoneNumber) {

        if (StringUtils.isEmpty(fullName) || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(id)
                || StringUtils.isEmpty(phoneNumber)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (this.managerService.findByUsername(id) != null || this.managerService.findByEmail(email) != null){
            return Response.status(Response.Status.CONFLICT).build();
        }

        if (!ValidationUtils.isIdCorrect(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!SecurityUtils.isPasswordCorrect(id,password)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Manager newUser = new Manager();
        newUser.setUsername(id);
        newUser.setName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhoneNumber(phoneNumber);
        this.managerService.create(newUser);

        return Response.ok().build();
    }
}
