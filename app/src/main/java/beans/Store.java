package beans;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class Store extends Buy {


    private float income ;

    public Store(String name, float price,float income) {
        super(name, price);
        this.income=income;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }


    public static ArrayList<Store> initStore(Context context){

        ArrayList<Store> stores = new ArrayList<>();

        String json;
        InputStream is=null;
        try{

            if(Locale.getDefault().getLanguage().equals("en"))
                is =context.getAssets().open("store.json");

            if(Locale.getDefault().getLanguage().equals("fr"))
                is =context.getAssets().open("store-fr.json");

            if(Locale.getDefault().getLanguage().equals("ar"))
                is =context.getAssets().open("store-ar.json");

            int size =is.available();

            byte[] buffer =new byte[size];
            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for (int i =0 ;i< jsonArray.length();i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                String name =jsonObject.getString("name");
                float price =jsonObject.getLong("price");
                float income=jsonObject.getLong("income");
                Store store =new Store(name,price,income);

                stores.add(store);
            }

        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        return stores;
    }

}
