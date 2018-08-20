package beans;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
        try{
            InputStream is = context.getAssets().open("store.json");

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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stores;
    }

}
