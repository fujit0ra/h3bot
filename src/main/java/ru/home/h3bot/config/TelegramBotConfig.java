package ru.home.h3bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.home.h3bot.HotaHandler;

@Configuration

public class TelegramBotConfig {
    private final HotaHandler hotaHandler;

    public TelegramBotConfig(HotaHandler hotaHandler) {
        this.hotaHandler = hotaHandler;
    }

    @Bean
    TelegramBotsApi telegramBotsApi(){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(hotaHandler);
            return botsApi;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
