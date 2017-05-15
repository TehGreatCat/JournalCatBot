package gov.nasa.Telegram;

import com.google.gson.JsonObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
    private long chat_id;

    private HttpClient httpClient = HttpClientBuilder.create().build();

    public BotMessageHandler(Update update) {
        this.update = update;
        this.chat_id = update.getMessage().getChatId();
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
            case "/help" : new SendMessage().setText("Help WIP").
                    setChatId(chat_id);//метод, который посылает инструкции к боту
            case "/register" : UserHandler();
            case "/findbydate": DateSearch();
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
            new SendMessage().setText("Geez, some serious stuff happened with encoding").
                    setChatId(chat_id);

        }

        // POST НА СОЗДАНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ
        if (StatusCodeEquals(404, resp)) {
            HttpPost reqst = new HttpPost("progerdodger-163116.appspot.com/users?token=" + id.toString());
            resp = httpClient.execute(reqst);
            if (StatusCodeEquals(201, resp)) {
                new SendMessage().setText("You are successfully registered!").
                        setChatId(chat_id);
            }
        } else {
            new SendMessage().setText("You are already registered").
                    setChatId(chat_id);
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

                //Main requests
                if (jsonParams.length() != 0) {
                    String location = "";
                    String text = "";
                    String timestamp = jsonParams.getJSONObject("system-time").get("date-time").toString();

                    if (!jsonParams.get("Location").toString().equals("")) {
                        location = jsonParams.get("Location").toString();
                    }
                    if (!jsonParams.get("system-location").toString().equals("")) {
                        location = jsonParams.get("system-location").toString();
                    }
                    if (jsonParams.has("music-artist") && !location.equals("")) {
                        location = location + " " + jsonParams.get("music-artist").toString();
                    }
                    if (!jsonParams.get("NoteText").toString().equals("")) {
                        text = jsonParams.get("NoteText").toString();
                        NeedsUpdate = false;
                    } else {
                        NeedsUpdate = true;
                        new SendMessage().setText("Give your Note a name: ").
                                setChatId(chat_id);
                    }

                    if (!NeedsUpdate) {
                        // note creation

                        String qloc = location.replaceAll(" ","+");
                        //request to google search
                        HttpGet googlerqst = new HttpGet("https://www.googleapis.com/customsearch/v1?" +
                                "key=AIzaSyCnXqyQSg1uXIsHnriD-OteAVxyQ38OxE8" +
                                "&cx=011153072190739070346:rkt80qaf3fu" +
                                "&q="+location +
                                "&searchType=image" +
                                "&alt=json");
                        HttpResponse googleresp = httpClient.execute(googlerqst);
                        JSONObject imgAns = new JSONObject(EntityUtils.toString(googleresp.getEntity()));
                        String imageLink = imgAns.getJSONArray("items").getJSONObject(0).get("link").toString();
                        // Send a card to Telegram, that contains a number of results?
                        JSONObject alltogether = new JSONObject();
                        //create json with info
                        alltogether
                                .put("name", text)
                                .put("timestamp", timestamp)
                                .put("imageLink", imageLink);
                        // string http entity?
                        //StringEntity params = new StringEntity(alltogether.toString());//здесь мб нужно "details="
                        String id = update.getMessage().getFrom().getId().toString();
                        HttpPost reqst = new HttpPost("progerdodger-163116.appspot.com/notes/"
                                + alltogether.toString());
                        reqst.addHeader("Authorization", id);
                        HttpResponse noteresp = httpClient.execute(reqst);
                        if(StatusCodeEquals(201, noteresp)){
                            new SendMessage().setText("your note is successfully created!").setChatId(chat_id);
                        }

                    }
                } else {
                    //SmallTalk supposed to go here
                    String speech = jsonAns.getJSONObject("result").getJSONObject("fulfillment").
                            getJSONArray("messages").getJSONObject(0).getString("speech");
                    new SendMessage().setText(speech).setChatId(chat_id);
                }
            }
        }
    }

    private void DateSearch(){}

}


