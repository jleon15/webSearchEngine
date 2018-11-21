package psm.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import programathon.psm.core.conversation.service.ConversationService;
import programathon.psm.model.ConversationResponse;
import programathon.psm.support.flexjson.JSONSerializerBuilder;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Scope("request")
@Path("/conversation")
public class ConversationResource {

    @Autowired
    private ConversationService conversationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response startConversation() {
        ConversationResponse assistantResponse = this.conversationService.startConversation();
        return Response.ok().entity(JSONSerializerBuilder.getBasicSerializer().serialize(assistantResponse)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response continueConversation(@FormParam("id") String conversationId,
                                         @FormParam("response") String response) {
        ConversationResponse userResponse = new ConversationResponse();
        userResponse.setId(conversationId);
        userResponse.setResponse(response);

        ConversationResponse assistantResponse = this.conversationService.continueConversation(userResponse);
        return Response.ok().entity(JSONSerializerBuilder.getBasicSerializer().serialize(assistantResponse)).build();
    }

}
