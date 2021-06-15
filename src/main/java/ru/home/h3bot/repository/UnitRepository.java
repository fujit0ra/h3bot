package ru.home.h3bot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.home.h3bot.models.dao.UnitDAO;

/**
 * Репозиторий для работы с UnitDAO
 */
public interface UnitRepository extends CrudRepository<UnitDAO, Integer> {
}
