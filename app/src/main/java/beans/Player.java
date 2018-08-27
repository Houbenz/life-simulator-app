package beans;

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

    private ArrayList<String> acquiredDegress;



    public Player(){

        work=new Work();
        work.setName("none");
        level=new Level(0);
        acquiredDegress=new ArrayList<>();
        acquiredDegress.add("none");
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
