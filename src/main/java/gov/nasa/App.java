package gov.nasa;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.googlecode.objectify.ObjectifyService;
import gov.nasa.Telegram.MainBot;
import gov.nasa.config.ComponentBinder;
import gov.nasa.config.StorageBinder;
import gov.nasa.domain.Note;
import gov.nasa.domain.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class App extends ResourceConfig {
    public App() {

        ObjectifyService.register(User.class);
        ObjectifyService.register(Note.class);

        //ayy lmao
        register(new StorageBinder());
        register(new ComponentBinder());

        register(JacksonJaxbJsonProvider.class);
        register(UsersResource.class);
        register(NotesResource.class);
        register(AuthFeature.class);


        // Регистрация Бота и контекста
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try{
            botsApi.registerBot(new MainBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
