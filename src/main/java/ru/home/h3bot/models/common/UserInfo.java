package ru.home.h3bot.models.common;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.home.h3bot.models.dao.CastleDAO;
import ru.home.h3bot.models.dao.UnitDAO;

public class UserInfo {

    public UserInfo(User user) {
        this.user = user;
    }

    private int activeStep;

    private User user;

    private CastleDAO firstCastle;
    private UnitDAO firstUnit;
    private Integer countFirstUnit;

    private CastleDAO secondCastle;
    private UnitDAO secondUnit;
    private Integer countSecondUnit;


    public int getActiveStep() {
        return activeStep;
    }

    public void setActiveStep(int activeStep) {
        this.activeStep = activeStep;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CastleDAO getFirstCastle() {
        return firstCastle;
    }

    public void setFirstCastle(CastleDAO firstCastle) {
        this.firstCastle = firstCastle;
    }

    public UnitDAO getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(UnitDAO firstUnit) {
        this.firstUnit = firstUnit;
    }

    public Integer getCountFirstUnit() {
        return countFirstUnit;
    }

    public void setCountFirstUnit(Integer countFirstUnit) {
        this.countFirstUnit = countFirstUnit;
    }

    public CastleDAO getSecondCastle() {
        return secondCastle;
    }

    public void setSecondCastle(CastleDAO secondCastle) {
        this.secondCastle = secondCastle;
    }

    public UnitDAO getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(UnitDAO secondUnit) {
        this.secondUnit = secondUnit;
    }

    public Integer getCountSecondUnit() {
        return countSecondUnit;
    }

    public void setCountSecondUnit(Integer countSecondUnit) {
        this.countSecondUnit = countSecondUnit;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "\nactiveStep=" + activeStep +
                ", \nuser=" + user +
                ", \nfirstCastle=" + firstCastle +
                ", \nfirstUnit=" + firstUnit +
                ", \ncountFirstUnit=" + countFirstUnit +
                ", \nsecondCastle=" + secondCastle +
                ", \nsecondUnit=" + secondUnit +
                ", \ncountSecondUnit=" + countSecondUnit +
                '}';
    }
}
