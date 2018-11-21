package psm.ws.rest;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.child.service.ChildService;
import programathon.psm.core.naturalLanguage.service.NaturalLanguageService;
import programathon.psm.core.security.manager.service.ManagerService;
import programathon.psm.core.speechToText.service.SpeechToTextService;
import programathon.psm.core.symptom.picture.service.SymptomPictureService;
import programathon.psm.core.symptom.service.SymptomService;
import programathon.psm.model.Child;
import programathon.psm.model.Manager;
import programathon.psm.model.Symptom;
import programathon.psm.model.SymptomPicture;
import programathon.psm.support.SecurityUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static programathon.psm.support.flexjson.JSONSerializerBuilder.getBasicSerializer;

@Component
@Scope("request")
@Path("/symptom")
public class SymptomResource {

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private SymptomPictureService symptomPictureService;

    @Autowired
    private SpeechToTextService speechToTextService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ChildService childService;

    @Autowired
    private NaturalLanguageService naturalLanguageService;

    /**
     * Corresponds to the web service in charge of uploading a new picture for the symptoms logged user.
     * @param uploadedPicture corresponds to the user's picture.
     * @return not found if any of the parameters is missing, unauthorized if there is no logged user and
     * ok if everything went well.
     */
    @POST
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePicture(@FormDataParam("picture") FormDataBodyPart body,
                                         @FormDataParam("picture") InputStream uploadedPicture) throws IOException {

        if(uploadedPicture == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        byte[] targetArray = IOUtils.toByteArray(uploadedPicture);

        SymptomPicture symptomPicture = new SymptomPicture();
        symptomPicture.setImage(targetArray);

        this.symptomPictureService.create(symptomPicture);

        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receiveCase(@FormDataParam("audio") FormDataBodyPart audioBody,
                                @FormDataParam("audio") InputStream uploadedAudio,
                                @FormDataParam("picture") FormDataBodyPart pictureBody,
                                @FormDataParam("picture") InputStream uploadedPicture
                                /*@FormParam("childrenId") String childId*/) throws IOException {

        byte[] targetPicture = {0};
        String parsedText;

        Manager manager = this.managerService.findById(SecurityUtils.getLoggedInUser().getId());
//        Child child = this.childService.findById(childId);

        if(uploadedAudio == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

//        if(child == null)
//            return Response.status(Response.Status.NOT_FOUND).build();

        if(uploadedPicture != null)
            targetPicture = IOUtils.toByteArray(uploadedPicture);

        parsedText = this.speechToTextService.convertToText(uploadedAudio,audioBody.getMediaType().toString());

        this.naturalLanguageService.analyzeText(parsedText);

        /*
        Symptom symptom = new Symptom();
        symptom.setSymptomsText(parsedText);
        symptom.setManager(manager);
        symptom.setChild(child);
        this.symptomService.create(symptom);

        SymptomPicture symptomPicture = new SymptomPicture();
        symptomPicture.setImage(targetPicture);
        symptomPicture.setSymptom(symptom);
        this.symptomPictureService.create(symptomPicture);
        */

        return Response.ok().build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createSymptom(@FormParam("description") String description,
                                  @FormParam("relevantData") String relevantData,
                                  @FormParam("childId") String childId){
        Symptom symptom = new Symptom();
        symptom.setChild(this.childService.findById(childId));
        symptom.setRelevantData(relevantData);
        symptom.setSymptomsText(description);
        return Response.ok().entity(getBasicSerializer().serialize(symptom)).build();
    }

}
