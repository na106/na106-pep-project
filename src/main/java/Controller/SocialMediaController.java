package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.post("/messages", this::addMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccMessagesHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAcc = accountService.register(account);
        if(addedAcc == null){
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(addedAcc));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggingAcc = accountService.AccLogin(account);
        if(loggingAcc != null){
            ctx.json(mapper.writeValueAsString(loggingAcc));
        } else {
            ctx.status(401);
        }
    }
    
    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageById(msg_id);
        if(msg != null)
        {
            ctx.json(msg);
        }
    }

    private void getAccMessagesHandler(Context ctx) throws JsonProcessingException {
        int acc_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByAccId(acc_id));
    }

    private void addMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMsg = messageService.addMessage(message);
        if(addedMsg != null){
            ctx.json(mapper.writeValueAsString(addedMsg));
        } else {
            ctx.status(400);
        }
    }

    public void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMsg = messageService.updateMessage(message_id, message);
        if(updatedMsg == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMsg));
        }
    }

    public void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.deleteMessage(msg_id);
        if(msg == null)
        {
            ctx.status(200);
        } else{
            ctx.json(mapper.writeValueAsString(msg));
        }
    }
}