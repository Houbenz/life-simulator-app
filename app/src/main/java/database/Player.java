package database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import beans.Level;

@Entity
public class Player {

    @PrimaryKey
    private int id ;

    private String name ;

    private String work ;

    private double work_income;

    private String creation_date;

    private String work_image_path;

    private  int work_time;

    private double store_income;

    private double balance;

    private String dating ;

    private  double bank_deposit;

    private int day;
    private int hour;
    private int minute;

    private int level;
    private int level_progress;
    private int max_progress;

    @Ignore
    private Level level_object;

    private int healthbar;
    private int hungerbar;
    private int energybar;

    public Player(){
        this.level_object = new Level(0);
    }

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

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBank_deposit() {
        return bank_deposit;
    }

    public void setBank_deposit(double bank_deposit) {
        this.bank_deposit = bank_deposit;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel_progress() {
        return level_progress;
    }

    public void setLevel_progress(int level_progress) {
        this.level_progress = level_progress;
    }


    public String getWork_image_path() {
        return work_image_path;
    }

    public void setWork_image_path(String work_image_path) {
        this.work_image_path = work_image_path;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public double getWork_income() {
        return work_income;
    }

    public void setWork_income(double work_income) {
        this.work_income = work_income;
    }


    public double getStore_income() {
        return store_income;
    }

    public void setStore_income(double store_income) {
        this.store_income = store_income;
    }

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public Level getLevel_object() {
        return level_object;
    }

    public void setLevel_object(Level level_object) {
        this.level_object = level_object;
    }

    public int getHealthbar() {
        return healthbar;
    }

    public void setHealthbar(int healthbar) {
        this.healthbar = healthbar;
    }

    public int getHungerbar() {
        return hungerbar;
    }

    public void setHungerbar(int hungerbar) {
        this.hungerbar = hungerbar;
    }

    public int getEnergybar() {
        return energybar;
    }

    public void setEnergybar(int energybar) {
        this.energybar = energybar;
    }

    public int getMax_progress() {
        return max_progress;
    }

    public void setMax_progress(int max_progress) {
        this.max_progress = max_progress;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public void upgradeLevel(){

        getLevel_object().setLevel(getLevel_object().getLevel()+1);
        changeMaxProgress();
        getLevel_object().setMaxProgress(getLevel_object().getMaxProgress()+getLevel_object().getMaxProgress());
        getLevel_object().setProgressLevel(0);
    }

    public void changeMaxProgress(){
        getLevel_object().setMaxProgress(getLevel_object().getLevel()*100);
    }
}
