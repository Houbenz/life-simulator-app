package beans;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Houbenz on 19/07/2018.
 */

public class Furniture extends Buy {


    private String fournitureType ;


    public Furniture(String name, float price, String fournitureType) {
        super(name, price);
    this.fournitureType=fournitureType;
    }


    public String getFournitureType() {
        return fournitureType;
    }

    public void setFournitureType(String fournitureType) {
        this.fournitureType = fournitureType;
    }



    public static ArrayList<Furniture> initFourniture(Context context){

        ArrayList<Furniture> fournitures =new ArrayList<Furniture>();

        String json ;
        try
        {
            InputStream is = context.getAssets().open("furniture.json");

            int size=is.available();

            byte[] buffer =new byte[size];

            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");

            JSONArray jsonArray=new JSONArray(json);


            for (int i = 0; i <jsonArray.length() ; i++) {

                JSONObject jsonObject=jsonArray.getJSONObject(i);

                String name =jsonObject.getString("name");
                int price =jsonObject.getInt("price");
                String fournitureType=jsonObject.getString("fournitureType");
                Furniture fourniture =new Furniture(name,price,fournitureType);

                fournitures.add(fourniture);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fournitures;
    }
}

