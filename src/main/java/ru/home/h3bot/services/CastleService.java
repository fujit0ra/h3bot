package ru.home.h3bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.h3bot.models.CastleDAO;
import ru.home.h3bot.repository.CastleRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по раблоте с замками.
 */
@Service
public class CastleService {
    private final CastleRepository castleRepository;

    public CastleService(CastleRepository castleRepository) {
        this.castleRepository = castleRepository;
    }

    /**
     * Получить кнопки telegram-бота со всеми замками.
     *
     * @return {@link InlineKeyboardMarkup} - кнопки telegram-бота со всеми замками
     */
    public InlineKeyboardMarkup getCastlesButtons() {
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        Iterable<CastleDAO> castleDAOS = castleRepository.findAll();
        castleDAOS.forEach(castleDAO -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData("Вы выбрали " + castleDAO.getName());
            button.setText(castleDAO.getName());
            buttonList.add(button);
        });

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> table = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        buttonList.forEach(button -> {
            if (row.size() < 3) {
                row.add(button);
            } else {
                List<InlineKeyboardButton> newRow = new ArrayList<>(row);
                table.add(newRow);
                row.clear();
            }
        });
        if(!row.isEmpty()){
            table.add(row);
        }
        keyboardMarkup.setKeyboard(table);
        return keyboardMarkup;
    }
}
