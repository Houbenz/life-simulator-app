package beans;

/**
 * Created by Houbenz on 19/07/2018.
 */

public class Sleep {


    private String name ;
    private int value;
    private int energyPlus;

    public Sleep(String name, int value, int energyPlus) {
        this.name = name;
        this.value = value;
        this.energyPlus = energyPlus;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getEnergyPlus() {
        return energyPlus;
    }

    public void setEnergyPlus(int energyPlus) {
        this.energyPlus = energyPlus;
    }
}
