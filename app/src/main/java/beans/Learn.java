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
 * Created by Houbenz on 20/08/2018.
 */

public class Learn {

    private String name;
    private float price;
    private int id ;
    private String imgURL;
    private int progress ;


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Learn(String name ,float price){
        this.name=name;
        this.price=price;
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


    public static ArrayList<Learn> initLearn(Context context){
        ArrayList<Learn> learns =new ArrayList<>();

        String json;
        InputStream is=null;
        try{

          //  if(Locale.getDefault().getLanguage().equals("en"))

            if(Locale.getDefault().getLanguage().equals("fr"))
                is =context.getAssets().open("Learn-fr.json");
            else

                is =context.getAssets().open("Learn.json");

          //  if(Locale.getDefault().getLanguage().equals("ar"))
           //     is =context.getAssets().open("Learn-ar.json");

            int size =is.available();

            byte[] buffer =new byte[size];

            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");

            JSONArray jsonArray =new JSONArray(json);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                String name =jsonObject.getString("name");
                float price =jsonObject.getLong("price");
                Learn learn =new Learn(name,price);
                learn.setImgURL(jsonObject.getString("uri"));
                learn.setId(jsonObject.getInt("id"));
                learn.setProgress(jsonObject.getInt("progress"));
                learns.add(learn);
            }

        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }
        return learns;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
