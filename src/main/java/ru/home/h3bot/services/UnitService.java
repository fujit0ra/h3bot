package ru.home.h3bot.services;

import org.springframework.stereotype.Service;
import ru.home.h3bot.repository.UnitRepository;

@Service

public class UnitService {
    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }
}
