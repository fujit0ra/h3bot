package ru.home.h3bot.models;

import javax.persistence.*;

/**
 * DAO сущность юнитоа.
 */
@Entity
@Table(name = "UNITS")
public class UnitDAO {

    /**
     * Идентификатор.
     */
    @Id
    private int id;

    /**
     * Имя.
     */
    private String name;

    private int attack;

    private int defense;

    private int minDamage;

    private int maxDamage;

    private int hp;

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
