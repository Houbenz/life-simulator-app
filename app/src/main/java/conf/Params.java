package conf;

/**
 * Created by Houbenz on 23/08/2018.
 */

public class Params {


    //for leveling
    public static final  int XP_GAIN=25;

    //for Energy loss and gain
    public static final  int ENERGY_LOSS=8;
    public static final int ENERGY_GAIN_PER_HOUR=8;



    //for hunger loss (gains are directly from food.json file)
    public static final int HUNGER_LOSS_PER_HOUR=4;
    public static final int HUNGER_LOSS_PER_HOUR_IN_SLEEP=HUNGER_LOSS_PER_HOUR/3;


    //health loss
    public static final int HEALTH_LOSS_PER_HOUR_IN_SLEEP=HUNGER_LOSS_PER_HOUR/3;
    public static final int HEALTH_LOSS_PER_HOUR=4;
    public static final int HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY=4;

    //speed

    public final static int TIME_SPEED_NORMAL =500;
    public final static int TIME_SPEED_FAST =250;
    public final static int TIME_SPEED_ULTRA_FAST =125;
    public final static int TIME_SPEED_SUPER_FAST =75;



}
