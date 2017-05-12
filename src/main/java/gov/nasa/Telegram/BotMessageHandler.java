package gov.nasa.Telegram;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.User;
import gov.nasa.data.Datastore.UsersDatastore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BotMessageHandler {

    private final Update update;

    private HttpClient httpClient = HttpClientBuilder.create().build();

    public BotMessageHandler(Update update) {
        this.update = update;
    }

    public void Crossroads() throws IOException {
        if(update.getMessage().isCommand()){
            commandHandler(update.getMessage());
        }
        else {
            sendApiReq();
        }
    }

    private void commandHandler(Message message) throws IOException {
        switch (message.getText()) {
            case "/help" : new SendMessage().setText("Help WIP");
            case "/register" : UserHandler();
        }
    }

    private void UserHandler() throws IOException {
        User user = update.getMessage().getFrom();
        Integer id = user.getId();
        HttpResponse resp = null;
        try {
            //GET НА ПОИСК ПОЛЬЗОВАТЕЛЯ
            HttpGet req = new HttpGet("progerdodger-163116.appspot.com/users?token=" + id.toString());
            //req.addHeader();
            resp = httpClient.execute(req);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            new SendMessage().setText("Geez, some serious stuff happened with encoding");
        }

        if (!StatusCodeEquals(200, resp)|| !StatusCodeEquals(302, resp)){
            HttpPost reqst = new HttpPost("progerdodger-163116.appspot.com/users?token="+id.toString());
            resp = httpClient.execute(reqst);
            if (StatusCodeEquals(201, resp)){

            }
        }
        private boolean StatusCodeEquals(Integer integer, HttpResponse resp){
            return resp.getStatusLine().getStatusCode() == integer;
    }
    }
}
