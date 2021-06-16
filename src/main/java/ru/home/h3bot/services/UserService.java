package ru.home.h3bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.home.h3bot.models.common.CallbackData;
import ru.home.h3bot.models.common.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис по работе с пользлователями.
 */
@Service
public class UserService {

    private final Map<Long, UserInfo> userInfoList = new HashMap<>();

    /**
     * Поулчить данные пользователя по его идентификатору.
     *
     * @param userId - идентификатор пользователя
     * @return {@link UserInfo} - данные пользователя.
     */
    public UserInfo getUserInfoById(long userId) {
        if (userInfoList.containsKey(userId)) {
            return userInfoList.get(userId);
        }
        return null;
    }

    /**
     * Создать пользователя.
     *
     * @param user - телеграм пользователь
     */
    public void createUser(User user) {
        UserInfo userInfo = new UserInfo(user);
        userInfoList.put(user.getId(), userInfo);
    }

    public void updateUser(CallbackData callbackData) {

    }
}
