package ru.home.h3bot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.h3bot.models.common.CallbackData;
import ru.home.h3bot.models.dao.CastleDAO;
import ru.home.h3bot.repository.CastleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис по раблоте с замками.
 */
@Slf4j
@Service
public class CastleService {

    private final CastleRepository castleRepository;
    private final ObjectMapper objectMapper;

    public CastleService(CastleRepository castleRepository, ObjectMapper objectMapper) {
        this.castleRepository = castleRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Получить кнопки telegram-бота со всеми замками.
     *
     * @param step - шаг
     * @return {@link InlineKeyboardMarkup} - кнопки telegram-бота со всеми замками
     */
    public InlineKeyboardMarkup getCastlesButtons(int step) {
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        Iterable<CastleDAO> castleDAOS = castleRepository.findAll();
        castleDAOS.forEach(castleDAO -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String castlesCallbackData = getCastlesCallbackData(step, castleDAO);
            button.setCallbackData(castlesCallbackData);
            button.setText(castleDAO.getName());
            buttonList.add(button);
        });

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> table = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        buttonList.forEach(button -> {
            if (row.size() == 3) {
                List<InlineKeyboardButton> newRow = new ArrayList<>(row);
                table.add(newRow);
                row.clear();
            }
            row.add(button);
        });
        if (!row.isEmpty()) {
            table.add(row);
        }
        keyboardMarkup.setKeyboard(table);
        return keyboardMarkup;
    }

    /**
     * Ответ на событие нажатия кнопки для определенного шага.
     *
     * @param step      - шаг
     * @param castleDAO - DAO замка
     * @return {@link String} - ответ на событие нажатия кнопки для определенного шага.
     */
    private String getCastlesCallbackData(int step, CastleDAO castleDAO) {
        try {
            CallbackData callbackData = new CallbackData(step + 1);
            //Формируем ответ для первого шага
            Map<String, Object> data = new HashMap<>();
            data.put("castleId", castleDAO.getId());
            callbackData.setData(data);
            //Преобразовываем CallbackData в JSON для ответа
            return objectMapper.writeValueAsString(callbackData);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
