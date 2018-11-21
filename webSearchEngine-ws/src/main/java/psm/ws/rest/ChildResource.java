package psm.ws.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.child.service.ChildService;
import programathon.psm.core.security.manager.service.ManagerService;
import programathon.psm.core.treatment.service.TreatmentService;
import programathon.psm.model.*;
import programathon.psm.support.SecurityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static programathon.psm.support.flexjson.JSONSerializerBuilder.getChildrenSerializer;

@Component
@Scope("request")
@Path("/child")
public class ChildResource {

    @Autowired
    private ChildService childService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TreatmentService treatmentService;

    /**
     * Corresponds to the web service in charge of creating a new child.
     *
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addNewChild(@FormParam("fullName") String name,
                                @FormParam("id") String id,
                                @FormParam("age") Integer age,
                                @FormParam("gender") String gender,
                                @FormParam("relationship") String relationship,
                                @FormParam("treatments") String treatments,
                                @FormParam("diseases") String diseases,
                                @FormParam("ethnicGroup") String ethnicGroup
                                ) {

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(age.toString())
                || StringUtils.isEmpty(gender) || StringUtils.isEmpty(ethnicGroup)
                || StringUtils.isEmpty(relationship)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Manager manager = this.managerService.findByUsername(SecurityUtils.getLoggedInUser().getUsername());
        Child newChild = new Child();
        newChild.setManager(manager);
        newChild.setName(name);
        newChild.setIdNumber(id);
        newChild.setAge(age);
        newChild.setDiseases(diseases);
        newChild.setGender(Gender.valueOf(gender.toUpperCase()));
        newChild.setEthnicGroup(ethnicGroup);
        newChild.setRelationship(Relationship.valueOf(relationship.toUpperCase()));
        this.childService.create(newChild);
        Set<Treatment> childTreatments = this.treatmentService.deserialize(treatments,newChild);
        newChild.setTreatments(childTreatments);

        return Response.ok().build();
    }

    /**
     * Gets the posts of the logged in user.
     * @return
     */
    @GET
    @Path("/getChildren")
    public Response getAllChildren() {
        List<Child> children = this.childService.getAllChildren();
        if(children == null || children.size() == 0)
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok().entity(getChildrenSerializer().serialize(children)).build();
    }
}
