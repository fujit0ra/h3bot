package ru.home.h3bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.h3bot.models.CastleDAO;
import ru.home.h3bot.repository.CastleRepository;

import java.util.ArrayList;
import java.util.List;

@Service

public class CastleService {
    private final CastleRepository castleRepository;

    public CastleService(CastleRepository castleRepository) {
        this.castleRepository = castleRepository;
    }
    public InlineKeyboardMarkup getCastlesButtons(){
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        Iterable<CastleDAO> castleDAOS = castleRepository.findAll();
        castleDAOS.forEach(castleDAO -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(castleDAO.getName());
            buttonList.add(button);
        });
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard();
    }
}
