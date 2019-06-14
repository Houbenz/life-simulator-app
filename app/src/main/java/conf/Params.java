package conf;

import java.util.ArrayList;

/**
 * Created by Houbenz on 23/08/2018.
 */

public class Params {


    //for leveling
    public static final  int XP_GAIN=25;

    //for Energy loss and gain
    public static final  int ENERGY_LOSS=8;
    public static final int ENERGY_GAIN_PER_HOUR=8;


    public static final double START_BALANCE = 20000;


    //for hunger loss (gains are directly from food.json file)
    public static final int HUNGER_LOSS_PER_HOUR=4;
    public static final int HUNGER_LOSS_PER_HOUR_IN_SLEEP=HUNGER_LOSS_PER_HOUR/3;


    //health loss
    public static final int HEALTH_LOSS_PER_HOUR_IN_SLEEP=HUNGER_LOSS_PER_HOUR/3;
    public static final int HEALTH_LOSS_PER_HOUR=4;
    public static final int HEALTH_LOSS_PER_HOUR_IF_NO_ENERGY=4;

    //speed

    public final static int TIME_SPEED_NORMAL =350;
    public final static int TIME_SPEED_FAST =250;
    public final static int TIME_SPEED_ULTRA_FAST =125;
    public final static int TIME_SPEED_SUPER_FAST =75;


    // player earning

    public final static int NUMBER_OF_MULTI_INOCME = 2 ;
    public final static int NUMBER_OF_HOURS_BENEFIT = 5 ;


    public static final int HEALTH_VALUE=100;
    public static final int HUNGER_VALUE=100;
    public static final int ENERGY_VALUE=100;


    public static final int LEARN_TIME = 60 ;


    public static final int MIN_TIME_LOOKING_FOR_PARTNER=20000;
    public static final int MAX_TIME_LOOKING_FOR_PARTNER=70000;

    public static final int CHOCOLATE_BONUS=1;
    public static final int ROSES_BONUS=5;
    public static final int JEWELRY_BONUS=45;


    public static final int MINUS_RELATION=7;




    public static ArrayList<String> getTexts(){
        ArrayList<String> strings = new ArrayList<String >();

        strings.add("you go to clubs hoping to meet someone");
        strings.add("you changed direction and go to bars");
        strings.add("finally you change your mind and start introducing yourself to strangers");
        strings.add("you go to dating sites");
        strings.add("maybe introduce yourself to your neighbors");
        strings.add("You start a conversation with your coworkers");

        return strings;
    }



}
