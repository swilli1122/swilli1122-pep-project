package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    // list of desired endpoints pointing to methods below
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByUserHandler);

        return app;
    }

    private void postNewAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account validAccount = accountService.loginAttempt(account);
        if(validAccount != null) {
            context.json(mapper.writeValueAsString(validAccount));
        } else {
            context.status(401);
        }
    }

    private void postNewMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.newMessage(message);
        if(newMessage != null) {
            context.json(mapper.writeValueAsString(newMessage));
        } else {
            context.status(400);
        }
    }

    private void getMessageHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message newMessage = messageService.getMessageByID(message_id);
        if (newMessage != null) {
            context.json(mapper.writeValueAsString(newMessage));
        } else {
            context.json("");
        } 
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message newMessage = messageService.deleteMessageByID(message_id);
        if (newMessage != null) {
            context.json(mapper.writeValueAsString(newMessage));
        } else {
            context.json("");
        } 
    }

    // was trying to only extract the new message String from the context for a while before
    // I realized to just make it an Object 
    private void patchMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message update_message = mapper.readValue(context.body(), Message.class);
        Message patchMessage = messageService.patchMessageByID(message_id, update_message);
        if (patchMessage != null) {
            context.json(mapper.writeValueAsString(patchMessage));
        } else {
            context.status(400);
        }
    }

    private void getMessageByUserHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        System.out.println("Handler passing account_id: " + account_id);
        List<Message> messagesFromUser = messageService.getMessagesFromUserByID(account_id);
        context.json(messagesFromUser);
    }
}