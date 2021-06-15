package ru.home.h3bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.h3bot.models.CastleDAO;
import ru.home.h3bot.models.UnitDAO;
import ru.home.h3bot.repository.UnitRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис по работе с юнитами.
 */
@Service
public class UnitService {
    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Получить кнопки telegram-бота со всеми юнитами.
     *
     * @return {@link InlineKeyboardMarkup} - кнопки telegram-бота со всеми юнитами
     */
    public InlineKeyboardMarkup getUnitsButtons() {
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        Iterable<UnitDAO> castleDAOS = unitRepository.findAll();
        castleDAOS.forEach(unitDAO -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(unitDAO.getName());
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
