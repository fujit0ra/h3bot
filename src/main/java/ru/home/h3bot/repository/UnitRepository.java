package ru.home.h3bot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.home.h3bot.models.dao.UnitDAO;

import java.util.List;

/**
 * Репозиторий для работы с UnitDAO
 */
public interface UnitRepository extends CrudRepository<UnitDAO, Integer> {

    @Query(value = "SELECT * FROM UNITS u WHERE u.CASTLE_ID = :castleId", nativeQuery = true)
    List<UnitDAO> findUnitsByCastleId(int castleId);
}
