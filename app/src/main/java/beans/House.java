package beans;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Houbenz on 19/07/2018.
 */

public class House extends Buy {

    private int bonusE;
    private int bonusH;

    public House(String name, float price,int bonusE,int bonusH) {
        super(name, price);
        this.bonusE=bonusE;
        this.bonusH=bonusH;

    }

    public int getBonusE() {
        return bonusE;
    }

    public void setBonusE(int bonusE) {
        this.bonusE = bonusE;
    }

    public int getBonusH() {
        return bonusH;
    }

    public void setBonusH(int bonusH) {
        this.bonusH = bonusH;
    }

    public static ArrayList<House> initHouse(Context context) {
        ArrayList<House> houses = new ArrayList<>();

        String json;       InputStream is=null;
        try{

          //  if(Locale.getDefault().getLanguage().equals("en"))
                is =context.getAssets().open("house.json");

          //  if(Locale.getDefault().getLanguage().equals("fr"))
            //    is =context.getAssets().open("house-fr.json");

          //  if(Locale.getDefault().getLanguage().equals("ar"))
            //    is =context.getAssets().open("house-ar.json");


            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name =jsonObject.getString("name");
                float price=jsonObject.getLong("price");
                int bonusE=jsonObject.getInt("bonusE");
                int bonusH=jsonObject.getInt("bonusH");
                House house =new House(name,price,bonusE,bonusH);

                houses.add(house);

            }

        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }
        return houses;
    }

}
