package ru.home.h3bot.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * DAO сущность юнитоа.
 */
@Entity
@Table(name = "UNITS")
public class UnitDAO implements Serializable {

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
     * Атака.
     */
    @JsonIgnore
    private int attack;

    /**
     * Защита.
     */
    @JsonIgnore
    private int defense;

    /**
     * Минимальный урон.
     */
    @JsonIgnore
    private int minDamage;

    /**
     * Максимальный урон.
     */
    @JsonIgnore
    private int maxDamage;

    /**
     * Здоровье.
     */
    @JsonIgnore
    private int hp;

    /**
     * Замок которому принадлежит юнит.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "castle_id", nullable = false)
    private CastleDAO castle;

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

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public CastleDAO getCastle() {
        return castle;
    }

    public void setCastle(CastleDAO castle) {
        this.castle = castle;
    }
}
