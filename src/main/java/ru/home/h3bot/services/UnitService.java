package ru.home.h3bot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.h3bot.models.common.CallbackData;
import ru.home.h3bot.models.common.UserInfo;
import ru.home.h3bot.models.dao.UnitDAO;
import ru.home.h3bot.repository.UnitRepository;

import java.util.*;

/**
 * Сервис по работе с юнитами.
 */
@Slf4j
@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final ObjectMapper objectMapper;

    public UnitService(UnitRepository unitRepository, ObjectMapper objectMapper) {
        this.unitRepository = unitRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Получить кнопки telegram-бота со всеми юнитами.
     *
     * @return {@link InlineKeyboardMarkup} - кнопки telegram-бота со всеми юнитами
     */
    public InlineKeyboardMarkup getUnitsButtons(List<UnitDAO> unitDAOList, int step) {
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        unitDAOList.forEach(unitDAO -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String callbackData = getUnitsCallbackData(step, unitDAO);
            button.setCallbackData(callbackData);
            button.setText(unitDAO.getName());
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

    public String calculateDamage(UserInfo userInfo) {
        if (userInfo != null
                && userInfo.getFirstUnit() != null
                && userInfo.getSecondUnit() != null
                && userInfo.getCountFirstUnit() != null
                && userInfo.getCountSecondUnit() != null
        ) {
            UnitDAO unit1 = userInfo.getFirstUnit();
            UnitDAO unit2 = userInfo.getSecondUnit();

            int countUnit1 = userInfo.getCountFirstUnit();
            int countUnit2 = userInfo.getCountSecondUnit();

            float totalDamage = 0f;

            float baseDamage = randFloat(unit1.getMinDamage(), unit1.getMaxDamage());
            log.info("Базовый урон 1 создания (baseDamage):", baseDamage);

            if (unit1.getAttack() > unit2.getDefense()) {
                log.info("Атака атакующего стека {} = {} > защиты атакуемого {} = {}",
                        unit1.getName(), unit1.getAttack(), unit2.getName(), unit2.getDefense());

                totalDamage = baseDamage * countUnit1 * (1 + (unit1.getAttack() - unit2.getDefense()) * 0.05f);
            } else {
                log.info("Атака атакующего стека {} = {} < защиты атакуемого {} = {}",
                        unit1.getName(), unit1.getAttack(), unit2.getName(), unit2.getDefense());

                totalDamage = baseDamage * countUnit1 / (1 + (unit2.getDefense() - unit1.getAttack()) * 0.05f);
            }

            float unit1stackHP = unit1.getHp() * countUnit1;

            float unit2stackHP = unit2.getHp() * countUnit2;

            int deathCountUnit2 = (int) (totalDamage / unit2.getHp());

            String stackName1 = unit1.getName() + "(" + countUnit1 + ")";
            String stackName2 = unit2.getName() + "(" + countUnit2 + ")";

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("Атакующий стек: " + stackName1)
                    .append("\n  Общее здоровье стека: " + unit1stackHP)
                    .append("\n  Итоговый нанесенный урон по " + stackName2 + ": " + totalDamage)
                    .append("\n\nАтакуемый стек: " + stackName2)
                    .append("\n  Общее здоровье стека: " + unit2stackHP)
                    .append("\n  Итоговый полученный урон от " + stackName1 + ": " + totalDamage)
                    .append("\n  Умрет существ: " + deathCountUnit2)
                    .append("\n\nРассчитать заного: /start");
            return stringBuilder.toString();
        }
        return null;
    }

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }


    /**
     * Ответ на событие нажатия кнопки для определенного шага.
     *
     * @param step     - шаг
     * @param unitsDAO - DAO юнита
     * @return {@link String} - ответ на событие нажатия кнопки для определенного шага.
     */
    private String getUnitsCallbackData(int step, UnitDAO unitsDAO) {
        try {
            CallbackData callbackData = new CallbackData(step + 1);
            //Формируем ответ для первого шага
            Map<String, Object> data = new HashMap<>();
            data.put("unitId", unitsDAO.getId());
            callbackData.setData(data);
            //Преобразовываем CallbackData в JSON для ответа
            return objectMapper.writeValueAsString(callbackData);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
