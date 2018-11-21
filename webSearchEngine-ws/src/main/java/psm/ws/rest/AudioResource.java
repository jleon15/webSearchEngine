package psm.ws.rest;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.naturalLanguage.service.impl.IbmNaturalLanguageService;
import programathon.psm.core.speechToText.service.SpeechToTextService;
import programathon.psm.model.Manager;
import programathon.psm.support.SecurityUtils;
import programathon.psm.support.flexjson.JSONSerializerBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
@Scope("request")
@Path("/audio")
public class AudioResource {

    @Autowired
    SpeechToTextService speechToTextService;

    /**
     * Corresponds to the web service in charge of uploading a new profile picture for the logged user.
     * @param uploadedAudio corresponds to the user's new audio.
     * @return not found if any of the parameters is missing, unauthorized if there is no logged user and
     * ok if everything went well.
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePicture(@FormDataParam("audio") FormDataBodyPart body,
                                         @FormDataParam("audio") InputStream uploadedAudio) throws FileNotFoundException {

        if(uploadedAudio == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String test = this.speechToTextService.convertToText(uploadedAudio,body.getMediaType().toString());

        System.out.println(body.getMediaType().toString());
        System.out.println(test);

        Manager manager = SecurityUtils.getLoggedInUser();
        if(manager == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();

        return Response.ok().build();
    }

}
