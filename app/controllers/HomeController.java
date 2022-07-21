package controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Poll;
import models.forms.PollForm;
import play.mvc.*;
import play.routing.JavaScriptReverseRouter;

import java.util.List;
import java.util.UUID;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(Http.Request request) {
        return ok(views.html.index.render(request));
    }

    public Result javascriptRoutes(Http.Request request) {
        return ok(JavaScriptReverseRouter.create(
                "createRoutes",
                "xhr",
                request.host(),
                routes.javascript.HomeController.save()
        )).as(Http.MimeTypes.JAVASCRIPT);
    }



    public Result all(Http.Request request){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ArrayNode out = objectMapper.getNodeFactory().arrayNode();
        List<Poll> polls = Poll.all();
        if(polls.size() >= 1){
            for (Poll task : polls) {
                ObjectNode mainnoode = objectMapper.valueToTree(task);
                out.add(mainnoode);
            }
        }

        return ok(out);
    }

    public Result delete(Http.Request request, UUID uuid){
        try{
            if(uuid == null || uuid.toString().trim() == ""){
                return badRequest("UUID is empty!");
            }

            System.out.println("uuid is" + uuid);

            Poll poll = Poll.findById(uuid);
            poll.delete();
            return ok();

        } catch (Exception e){
            e.printStackTrace();
            return internalServerError();
        }
    }

    public Result save(Http.Request request) throws JsonProcessingException {
        String body = request.body().asJson().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        PollForm pollForm =  objectMapper.readValue(body, PollForm.class);
        System.out.println("poll ++==+=+++=++" + pollForm.question);
       if(pollForm.uuid == null || pollForm.uuid.equals("")){
           pollForm.uuid = UUID.randomUUID();
       }

       Poll poll = new Poll();
       poll.question = pollForm.question;
       poll.answerb = pollForm.answerb;
       poll.answera = pollForm.answera;
       poll.votesa = pollForm.votesa;
       poll.votesb = pollForm.votesb;
       poll.save();
        ObjectNode kopfNode = objectMapper.valueToTree(poll);
        ArrayNode out = objectMapper.getNodeFactory().arrayNode();
        out.add(kopfNode);
        return ok(out);
    }

    public Result vote(Http.Request request) throws JsonProcessingException {
        String body = request.body().asJson().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        PollForm pollForm =  objectMapper.readValue(body, PollForm.class);

        try{
            if(pollForm.uuid == null || pollForm.uuid.toString().trim() == ""){
                return badRequest("UUID is empty!");
            }

            Poll poll = Poll.findById(pollForm.uuid);
            if(poll != null){
                poll.votesb += pollForm.votesb;
                poll.votesa += pollForm.votesa;
            }
            poll.update();
            ObjectNode kopfNode = objectMapper.valueToTree(poll);
            ArrayNode out = objectMapper.getNodeFactory().arrayNode();
            out.add(kopfNode);
            return ok(out);

        } catch (Exception e){
            e.printStackTrace();
            return internalServerError();
        }

    }

}
