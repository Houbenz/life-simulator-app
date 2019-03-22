package beans;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Houbenz on 19/07/2018.
 */

public class Buy {


    private String name ;
    private float price ;
    private String color;
    private String imagePath;

    public Buy(){

    }

    public Buy(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public Buy(String name, float price,String color) {
        this.name = name;
        this.price = price;
        this.color=color;
    }
    public Buy(String name, float price,String color,String imagePath) {
        this.name = name;
        this.price = price;
        this.color=color;
        this.imagePath=imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static ArrayList<Buy> initBuy(Context context){

        ArrayList<Buy> buys = new ArrayList<>();

        String json ;
        InputStream is=null;
        try{

            //if(Locale.getDefault().getLanguage().equals("en"))
            is =context.getAssets().open("buy.json");

           // if(Locale.getDefault().getLanguage().equals("fr"))
             //   is =context.getAssets().open("buy-fr.json");

           // if(Locale.getDefault().getLanguage().equals("ar"))
             //   is =context.getAssets().open("buy-ar.json");

            int size =is.available();

            byte[] buffer =new byte[size];

            is.read(buffer);
            is.close();

            json =new String(buffer,"UTF-8");

            JSONArray jsonArray= new JSONArray(json);

            for (int i = 0; i <jsonArray.length() ; i++) {

                JSONObject jsonObject= jsonArray.getJSONObject(i);
                String name=jsonObject.getString("name") ;
                String color =jsonObject.getString("color");
                String imagePath =jsonObject.getString("uri");
                Buy buy =new Buy(name,0,color,imagePath);

                buys.add(buy);

            }

        }catch (IOException |JSONException e) {
            e.printStackTrace();
        }

        return buys;
    }
}
