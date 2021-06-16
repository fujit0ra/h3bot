package ru.home.h3bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.h3bot.models.common.CallbackData;
import ru.home.h3bot.models.common.UserInfo;
import ru.home.h3bot.models.dao.UnitDAO;
import ru.home.h3bot.repository.CastleRepository;
import ru.home.h3bot.repository.UnitRepository;
import ru.home.h3bot.services.CastleService;
import ru.home.h3bot.services.UnitService;
import ru.home.h3bot.services.UserService;

import java.util.List;

/**
 * Оброботчик команд от Hota-бота.
 */
@Slf4j
@Component
public class HotaHandler extends TelegramLongPollingBot {

    private final UnitService unitService;
    private final CastleService castleService;
    private final ObjectMapper objectMapper;
    private final UnitRepository unitRepository;
    private final CastleRepository castleRepository;
    private final UserService userService;

    @Value("${telegram-connection.token}")
    private String token;

    @Value("${telegram-connection.username}")
    private String username;

    public HotaHandler(UnitService unitService, CastleService castleService, ObjectMapper objectMapper, UnitRepository unitRepository, CastleRepository castleRepository, UserService userService) {
        this.unitService = unitService;
        this.castleService = castleService;
        this.objectMapper = objectMapper;
        this.unitRepository = unitRepository;
        this.castleRepository = castleRepository;
        this.userService = userService;
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

            User telegramUser = update.getMessage().getFrom();
            UserInfo userInfo = userService.getUserInfoById(telegramUser.getId());

            //Событие нажатия "/start"
            if (update.getMessage() != null && update.getMessage().getText() != null && update.getMessage().getText().equals("/start")) {
                InlineKeyboardMarkup castlesButtons = castleService.getCastlesButtons(1);
                sendMessage(castlesButtons, update.getMessage().getChatId().toString(), "Выберите замок атакующего существа");
                userService.createUser(telegramUser);
            } else if (update.getMessage() != null && update.getMessage().getText() != null && userInfo != null) {
                if (userInfo.getActiveStep() == 4) {
                    log.warn("Вызываем шаг {}", userInfo.getActiveStep());
                    log.info("Данные пользователя: {}", userInfo);
                    try {
                        int countFirstUnit = Integer.parseInt(update.getMessage().getText());
                        if (countFirstUnit > 0) {
                            userInfo.setCountFirstUnit(countFirstUnit);
                            InlineKeyboardMarkup castlesButtons = castleService.getCastlesButtons(userInfo.getActiveStep());
                            sendMessage(castlesButtons, update.getMessage().getChatId().toString(), "Выберите замок атакуемого существа");
                            userInfo.setActiveStep(userInfo.getActiveStep() + 1);
                        } else {
                            sendMessage(null, update.getMessage().getChatId().toString(), "Количество существ не может быть меньше 0");
                        }
                    } catch (NumberFormatException e) {
                        log.error("Неверный формат", e.getMessage());
                        sendMessage(null, update.getMessage().getChatId().toString(), "Неверный формат");
                    }
                } else if (userInfo.getActiveStep() == 7) {
                    log.warn("Вызываем шаг {}", userInfo.getActiveStep());
                    log.info("Данные пользователя: {}", userInfo);
                    try {
                        int countSecondUnit = Integer.parseInt(update.getMessage().getText());
                        if (countSecondUnit > 0) {
                            userInfo.setCountSecondUnit(countSecondUnit);
                            String resultMessage = unitService.calculateDamage(userInfo);
                            boolean isSend = sendMessage(null, update.getMessage().getChatId().toString(), resultMessage);
                            if (isSend) {
                                userInfo.setActiveStep(0);
                            }
                        } else {
                            sendMessage(null, update.getMessage().getChatId().toString(), "Количество существ не может быть меньше 0");
                        }
                    } catch (NumberFormatException e) {
                        log.error("Неверный формат", e.getMessage());
                        sendMessage(null, update.getMessage().getChatId().toString(), "Неверный формат");
                    }
                }
            } else {
                sendMessage(null, update.getMessage().getChatId().toString(), "Для начала работы бота введите /start");
            }

        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery() != null) {

                //Получаем данные вызова
                User telegramUser = update.getCallbackQuery().getFrom();
                String jsonData = update.getCallbackQuery().getData();
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

                log.info("Ответ от нажатия на кнопку: {}", jsonData);

                try {
                    CallbackData callbackData = objectMapper.readValue(jsonData, CallbackData.class);
                    UserInfo userInfo = userService.getUserInfoById(telegramUser.getId());

                    if (callbackData != null) {
                        if (callbackData.getStep() == 2) {
                            log.warn("Вызываем шаг {}", callbackData.getStep());
                            log.info("Данные пользователя: {}", userInfo);
                            if (callbackData.getData() != null && callbackData.getData().containsKey("castleId")) {
                                int castleId = (int) callbackData.getData().get("castleId");
                                log.info("Получен идентификатор замка: {}", castleId);
                                List<UnitDAO> units = unitRepository.findUnitsByCastleId(castleId);
                                log.info("Получены юниты: {}", units);
                                InlineKeyboardMarkup unitsButtons = unitService.getUnitsButtons(units, callbackData.getStep());
                                sendMessage(unitsButtons, chatId, "Выберите атакующее существо");

                                userInfo.setActiveStep(callbackData.getStep());
                                if (castleRepository.findById(castleId).isPresent()) {
                                    userInfo.setFirstCastle(castleRepository.findById(castleId).get());
                                }
                            }
                        } else if (callbackData.getStep() == 3) {
                            log.warn("Вызываем шаг {}", callbackData.getStep());
                            log.info("Данные пользователя: {}", userInfo);
                            if (callbackData.getData() != null && callbackData.getData().containsKey("unitId")) {
                                int unitId = (int) callbackData.getData().get("unitId");
                                log.info("Получени идентификатор атакующего существа: {}", unitId);
                                sendMessage(null, chatId, "Введите количество атакующих существ");

                                userInfo.setActiveStep(callbackData.getStep() + 1);
                                if (unitRepository.findById(unitId).isPresent()) {
                                    userInfo.setFirstUnit(unitRepository.findById(unitId).get());
                                }
                            }
                        } else if (callbackData.getStep() == 5) {
                            log.warn("Вызываем шаг {}", callbackData.getStep());
                            log.info("Данные пользователя: {}", userInfo);
                            if (callbackData.getData() != null && callbackData.getData().containsKey("castleId")) {
                                int castleId = (int) callbackData.getData().get("castleId");
                                log.info("Получен идентификатор замка: {}", castleId);
                                List<UnitDAO> units = unitRepository.findUnitsByCastleId(castleId);
                                log.info("Получены юниты: {}", units);
                                InlineKeyboardMarkup unitsButtons = unitService.getUnitsButtons(units, callbackData.getStep());
                                sendMessage(unitsButtons, chatId, "Выберите атакуемое существо");

                                userInfo.setActiveStep(callbackData.getStep());
                                if (castleRepository.findById(castleId).isPresent()) {
                                    userInfo.setSecondCastle(castleRepository.findById(castleId).get());
                                }
                            }
                        } else if (callbackData.getStep() == 6) {
                            log.warn("Вызываем шаг {}", callbackData.getStep());
                            log.info("Данные пользователя: {}", userInfo);
                            if (callbackData.getData() != null && callbackData.getData().containsKey("unitId")) {
                                int unitId = (int) callbackData.getData().get("unitId");
                                log.info("Получени идентификатор атакующего существа: {}", unitId);
                                sendMessage(null, chatId, "Введите количество атакуемых существ");

                                userInfo.setActiveStep(callbackData.getStep() + 1);
                                if (unitRepository.findById(unitId).isPresent()) {
                                    userInfo.setSecondUnit(unitRepository.findById(unitId).get());
                                }
                            }
                        }
                    }

                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Отправить сообщение.
     *
     * @param buttons - кнопки
     * @param chatId  - идентификатор чата
     * @param text    - текст сообщения
     */
    public boolean sendMessage(InlineKeyboardMarkup buttons, String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(buttons);
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message); // Call method to send the message
            return true;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}