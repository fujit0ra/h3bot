package ru.home.h3bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.h3bot.services.CastleService;
import ru.home.h3bot.services.UnitService;

/**
 * Оброботчик команд от Hota-бота.
 */
@Component
public class HotaHandler extends TelegramLongPollingBot {

    private final UnitService unitService;
    private final CastleService castleService;

    @Value("${telegram-connection.token}")
    private String token;

    @Value("${telegram-connection.username}")
    private String username;

    public HotaHandler(UnitService unitService, CastleService castleService) {
        this.unitService = unitService;
        this.castleService = castleService;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            InlineKeyboardMarkup castlesButtons = castleService.getCastlesButtons(1);
            sendMessage(castlesButtons, update.getMessage().getChatId().toString(), "Выберите замок" );
        } else if (update.hasCallbackQuery()) {
            System.out.println(update);
        }

    }
    public void sendMessage(InlineKeyboardMarkup buttons, String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(buttons);
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}