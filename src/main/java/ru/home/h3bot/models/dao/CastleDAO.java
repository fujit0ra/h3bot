package ru.home.h3bot.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * DAO сущность замка.
 */
@Entity
@Table(name = "CASTLES")
public class CastleDAO implements Serializable {

    /**
     * Идентификатор.
     */
    @Id
    private int id;

    /**
     * Имя.
     */
    private String name;

    /**
     * Юниты замка.
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "castle")
    private Set<UnitDAO> units;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UnitDAO> getUnits() {
        return units;
    }

    public void setUnits(Set<UnitDAO> units) {
        this.units = units;
    }
}
