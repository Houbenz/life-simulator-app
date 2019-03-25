package beans;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Houbenz on 17/08/2018.
 */

public class Medicine extends  Buy{



    private  int benefit ;
    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
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
        InputStream is=null;

        try{

          //  if(Locale.getDefault().getLanguage().equals("en"))
                is =context.getAssets().open("medicine.json");

           // if(Locale.getDefault().getLanguage().equals("fr"))
           //     is =context.getAssets().open("medicine-fr.json");

            //if(Locale.getDefault().getLanguage().equals("ar"))
            //    is =context.getAssets().open("medicine-ar.json");

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

                medicine.setId(jsonObject.getInt("id"));
                medicines.add(medicine);
            }

        }catch (IOException|JSONException e){
            e.printStackTrace();
        }

        return medicines;
    }

}
