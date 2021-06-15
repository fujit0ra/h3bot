package ru.home.h3bot.models.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Сущность ответа на нажатие кновпки.
 *
 * @author PAnisimov on 15.06.2021
 */
public class CallbackData {

    public CallbackData(int step) {
        this.step = step;
    }

    /**
     * Шаг.
     */
    private int step;

    /**
     * Мета-данные шага.
     */
    private Map<String, Object> data = new HashMap<>();

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
