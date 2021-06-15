package ru.home.h3bot.models.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Сущность ответа на нажатие кновпки.
 */
public class CallbackData {

    public CallbackData(int step) {
        this.step = step;
    }

    public CallbackData(int step, Map<String, Object> data) {
        this.step = step;
        this.data = data;
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
