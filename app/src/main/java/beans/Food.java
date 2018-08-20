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

public class Food extends Buy {


    private String description ;
    private int benefit ;
    private String imagePath;



    public Food(String name, float price, String foodType, int benefit,String imagePath) {
        super(name, price);
        this.description=foodType;
        this.benefit=benefit;
        this.imagePath=imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBenefit() {return benefit;}

    public void setBenefit(int benefit) {this.benefit = benefit;}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }





    public static ArrayList<Food> initFood(Context context){

        ArrayList<Food> foods =new ArrayList<Food>();

        String json ;
        try{
            InputStream is = context.getAssets().open("food.json");

            int size =is.available();


            byte[] buffer =new byte[size];


            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");


            JSONArray jsonArray=new JSONArray(json);


            for (int i=0; i<jsonArray.length();i++){

                JSONObject jsonObject=jsonArray.getJSONObject(i);

                String name =jsonObject.getString("name");
                float price = jsonObject.getLong("price");
                int benefit= jsonObject.getInt("benefit");
                String description=jsonObject.getString("description");
                String imgPath =jsonObject.getString("uri");

                Food food=new Food(name,price,description,benefit,imgPath);

                foods.add(food);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  foods;
    }


}
