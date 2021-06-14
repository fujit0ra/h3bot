package ru.home.h3bot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.home.h3bot.models.UnitDAO;

public interface UnitRepository extends CrudRepository<UnitDAO, Integer> {
}
