package psm.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.notifications.service.NotificationService;
import programathon.psm.core.security.manager.service.ManagerService;
import programathon.psm.model.Manager;
import programathon.psm.model.Notification;
import programathon.psm.support.SecurityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static programathon.psm.support.flexjson.JSONSerializerBuilder.getBasicSerializer;

@Component
@Scope("request")
@Path("/notification")
public class NotificationResource {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ManagerService managerService;

    @GET
    @Path("/getNotifications")
    public Response getManagerNotifications() {
        Manager manager = this.managerService.findByUsername(SecurityUtils.getLoggedInUser().getUsername());
        List<Notification> notifications = this.notificationService.getManagerNotification(manager.getId());
        if(notifications == null || notifications.size() == 0)
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok().entity(getBasicSerializer().serialize(notifications)).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createNotification() {

     return null;
    }


}
