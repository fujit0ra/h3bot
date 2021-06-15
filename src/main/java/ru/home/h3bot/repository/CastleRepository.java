package ru.home.h3bot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.home.h3bot.models.CastleDAO;

/**
 * Репозиторий для работы с CastleDAO
 */
public interface CastleRepository extends CrudRepository<CastleDAO, Integer> {
}
