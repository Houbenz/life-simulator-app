package beans;

import android.content.Context;

import com.example.android.testsharedpreferences.R;

import java.util.ArrayList;

/**
 * Created by Houbenz on 30/07/2018.
 */

public class Player {

    private String name ;
    private float balance ;
    private Work work ;
    private int workMinutes;
    private Level level;
    private float bankDeposit;


    private float storeIncome;


    private int houseHealthBenefit;
    private int houseEnegryBenefit;

    private ArrayList<String> acquiredDegress;
    private ArrayList<String> acquiredStores;
    private ArrayList<String> acquiredHouses;



    public Player(Context context){

        work=new Work();
        work.setName(context.getString(R.string.none));
        level=new Level(0);
        acquiredDegress=new ArrayList<>();
        acquiredDegress.add(context.getString(R.string.none));
    }

    public Player(String name, int income, int balance, Work work,int bankDeposit) {
        this.name = name;
        this.balance = balance;
        this.work = work;
        workMinutes=getWork().getTimeOfWork()*60;
        acquiredDegress=new ArrayList<>();
        this.bankDeposit=bankDeposit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public int getWorkMinutes() {
        return workMinutes;
    }

    public void setWorkMinutes(int workMinutes) {
        this.workMinutes = workMinutes;
    }

    public Level getLevel(){return level;}

    public void setLevel(Level level){this.level=level;}

    public ArrayList<String> getAcquiredDegress() {
        return acquiredDegress;
    }

    public void setAcquiredDegress(ArrayList<String> acquiredDegress) {
        this.acquiredDegress = acquiredDegress;
    }
    /** return how much the player have in bank **/
    public float getBankDeposit() {return bankDeposit;}

    public void setBankDeposit(float bankDeposit) {this.bankDeposit = bankDeposit;}


    public ArrayList<String> getAcquiredStores() {
        return acquiredStores;
    }

    public void setAcquiredStores(ArrayList<String> acquiredStores) {
        this.acquiredStores = acquiredStores;
    }

    public ArrayList<String> getAcquiredHouses() {
        return acquiredHouses;
    }

    public void setAcquiredHouses(ArrayList<String> acquiredHouses) {
        this.acquiredHouses = acquiredHouses;
    }

    public float getStoreIncome() {
        return storeIncome;
    }

    public void setStoreIncome(float storeIncome) {
        this.storeIncome = storeIncome;
    }

    public int getHouseHealthBenefit() {
        return houseHealthBenefit;
    }

    public void setHouseHealthBenefit(int houseHealthBenefit) {
        this.houseHealthBenefit = houseHealthBenefit;
    }

    public int getHouseEnegryBenefit() {
        return houseEnegryBenefit;
    }

    public void setHouseEnegryBenefit(int houseEnegryBenefit) {
        this.houseEnegryBenefit = houseEnegryBenefit;
    }

    public void upgradeLevel(){

        getLevel().setLevel(getLevel().getLevel()+1);
        changeMaxProgress();
        getLevel().setMaxProgress(getLevel().getMaxProgress()+getLevel().getMaxProgress());
        getLevel().setProgressLevel(0);
    }

    public void changeMaxProgress(){
        getLevel().setMaxProgress(getLevel().getLevel()*100);
    }


}
