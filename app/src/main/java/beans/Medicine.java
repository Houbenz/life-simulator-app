package beans;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class Medicine extends  Buy{



    private  int benefit ;

    public Medicine(String name, float price,int benefit) {
        super(name,price);
        this.benefit=benefit;
    }


    public int getBenefit() {
        return benefit;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }

    public static ArrayList<Medicine> initMedicine(Context context){

        ArrayList<Medicine> medicines =new ArrayList<>();

        String json ;
        try {
            InputStream is = context.getAssets().open("medicine.json");

            int size =is.available();

            byte[] buffer=new byte[size];

            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");
            JSONArray jsonArray=new JSONArray(json);

            for(int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name =jsonObject.getString("name");
                float price =jsonObject.getLong("price");
                int benefit =jsonObject.getInt("benefit");

                Medicine medicine=new Medicine(name,price,benefit);

                medicines.add(medicine);
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return medicines;
    }

}