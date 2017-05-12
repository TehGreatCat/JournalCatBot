package gov.nasa.Telegram;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;

public class MainBot extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return "315369165:AAFBOBgyOAdjl6-YwMAIaMdf4IiQyJLbNTk";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // здесь надо проверить сообщение на наличие небоходимых элементов( изобр. текст етц)
        // здесь же должен быть поиск в гугле и запрос к api.ai в виде Json

        if (update.hasMessage() && update.getMessage().hasText()) {
            BotMessageHandler botMH = new BotMessageHandler(update);
            try {
                botMH.Crossroads();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "JournalCatBot";
    }
}
