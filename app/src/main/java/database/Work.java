package database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Work  implements Comparable<Work>{

    @PrimaryKey(autoGenerate = true)
    private int id ;

    private String name ;
    private String degree_required;
    private double income;
    private int work_time;
    private String imgPath;
    private int lvlToWork;
    private int degree_id;



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

    public String getDegree_required() {
        return degree_required;
    }

    public void setDegree_required(String degree_required) {
        this.degree_required = degree_required;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getLvlToWork() {
        return lvlToWork;
    }

    public void setLvlToWork(int lvlToWork) {
        this.lvlToWork = lvlToWork;
    }

    public int getDegree_id() {
        return degree_id;
    }

    public void setDegree_id(int degree_id) {
        this.degree_id = degree_id;
    }

    @Override
    public int compareTo(Work work) {

        if(this.getLvlToWork() > work.getLvlToWork()){
            return 1;
        }else
            return -1;
    }
}
