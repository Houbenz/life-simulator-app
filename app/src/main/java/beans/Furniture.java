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

public class Furniture extends Buy {


    private String fournitureType ;
    private String url ;

    public Furniture(String name, float price, String fournitureType,String  url) {
        super(name, price);
    this.fournitureType=fournitureType;
    this.url=url;
    }


    public String getFournitureType() {
        return fournitureType;
    }

    public void setFournitureType(String fournitureType) {
        this.fournitureType = fournitureType;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url=url;
    }


    public static ArrayList<Furniture> initFourniture(Context context){

        ArrayList<Furniture> fournitures =new ArrayList<Furniture>();

        String json ;       InputStream is=null;
        try{

            if(Locale.getDefault().getLanguage().equals("en"))
                is =context.getAssets().open("furniture.json");

            if(Locale.getDefault().getLanguage().equals("fr"))
                is =context.getAssets().open("furniture-fr.json");

            if(Locale.getDefault().getLanguage().equals("ar"))
                is =context.getAssets().open("furniture-ar.json");

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
                String imageUrl  = jsonObject.getString("url");
                Furniture fourniture =new Furniture(name,price,fournitureType,imageUrl);

                fournitures.add(fourniture);
            }

        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }

        return fournitures;
    }
}

