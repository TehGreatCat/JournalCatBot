package gov.nasa.Telegram;

import com.google.gson.JsonObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.User;
import gov.nasa.data.Datastore.UsersDatastore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BotMessageHandler {

    private final Update update;
    private boolean NeedsUpdate = false;

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
            case "/help" : new SendMessage().setText("Help WIP");//метод, который посылает инструкции к боту
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

        // POST НА СОЗДАНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ
        if (StatusCodeEquals(404, resp)) {
            HttpPost reqst = new HttpPost("progerdodger-163116.appspot.com/users?token=" + id.toString());
            resp = httpClient.execute(reqst);
            if (StatusCodeEquals(201, resp)) {
                new SendMessage().setText("You are successfully registered!");
            }
        } else {
            new SendMessage().setText("You are already registered");
        }
    }

    private boolean StatusCodeEquals(Integer integer, HttpResponse resp){
            return resp.getStatusLine().getStatusCode() == integer;
    }

    private void sendApiReq() throws IOException {
        Message message = update.getMessage();
        String msgText = message.getText();
        if (msgText != null){

            HttpGet req = new HttpGet("https://api.api.ai/v1/query?v=20150910&query=" + msgText +
                    "&lang=en" + "&sessionId="+"04513221337");
            req.addHeader("Authorization","Bearer b23935fb86a14f51b26872dbd9800d21");
            HttpResponse resp = httpClient.execute(req);

            if(StatusCodeEquals(200, resp)){

                JSONObject jsonAns = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject jsonParams = jsonAns.getJSONObject("result").getJSONObject("parameters");

                String location = "";
                String text = "";
                String timestamp = jsonParams.getJSONObject("system-time").get("date-time").toString();

                if(!jsonParams.get("Location").toString().equals("")){
                    location = jsonParams.get("Location").toString();
                }
                if(!jsonParams.get("system-location").toString().equals("")){
                    location = jsonParams.get("system-location").toString();
                }
                if(jsonParams.has("music-artist") && !location.equals("")){
                    location = location + " " + jsonParams.get("music-artist").toString();
                }
                if(!jsonParams.get("NoteText").toString().equals("")){
                    text = jsonParams.get("NoteText").toString();
                    NeedsUpdate = false;
                } else {
                    NeedsUpdate = true;
                    new SendMessage().setText("Give your Note a name: ");
                }

                if(!NeedsUpdate){
                    // note creation
                    // google img search or manual adding?
                }
            }
        }
    }


}


